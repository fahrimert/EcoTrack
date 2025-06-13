package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.SensorCountDTO;
import com.example.EcoTrack.dto.SensorDateCountDTO;
import com.example.EcoTrack.dto.SensorLocationDTO;
import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorStatus;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.RefreshTokenRepository;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.TaskRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
public class SuperVizorGraphsService {
    public  UserService userService;
    private  final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private  final TaskRepository taskRepository;
    private  final JwtService jwtService;
    private  final  RefreshTokenService refreshTokenService;
    private  final  OTPService otpService;
    private  final SensorSessionRepository sensorSessionRepository;
    private  final RefreshTokenRepository refreshTokenRepository;
    public SuperVizorGraphsService(UserService userService,UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, TaskRepository taskRepository, CustomUserDetailService userDetailServicee1, JwtService jwtService, RefreshTokenService refreshTokenService, OTPService otpService, SensorSessionRepository sensorSessionRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        this.authenticationManager = authenticationManager;
        this.taskRepository = taskRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.sensorSessionRepository = sensorSessionRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public List<SensorLocationDTO> getSensorSessionLocationsBasedOnUser (Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);

        List<SensorLocationDTO> sensorSessionss =sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user).stream().map(a -> {
                    SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

                    sensorLocationDTO.setId(a.getSensor().getId());
                    sensorLocationDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
                    sensorLocationDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());
                    return sensorLocationDTO;
                })
                .collect(Collectors.toMap(
                        SensorLocationDTO::getId,
                        dto -> dto,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream().collect(Collectors.toList());

        return sensorSessionss;
    }
    public  List<SensorLocationDTO> getFaultyLocationsOfAllSensors(){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        List<SensorLocationDTO> sensorSessions = sensorSessionRepository.findAllSensorFixesWithFaultySensors().stream().map(a -> {
            SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

            sensorLocationDTO.setId(a.getId());
            sensorLocationDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
            sensorLocationDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());
            return sensorLocationDTO;
        }).collect(Collectors.toList());

        return sensorSessions;
    }

    public  List<Map<String, Map<String, Long>>> getThePerformanceTableForWorker(){
        List<User> users = userRepository.findAllByRole(Role.WORKER);


        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();

        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        long maxAcceptableDurationMillis = 5L  * 60 * 60 * 1000;

        Map<String, Long> userAverageMinLastMonth = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<SensorFix> recentSessions = user.getSensorSessions().stream()
                                    .filter(session ->
                                            session.getCompletedTime() != null &&
                                                    session.getCompletedTime().after(oneMonthAgoa) &&
                                                    session.getCompletedTime().getTime() - session.getStartTime().getTime() <= maxAcceptableDurationMillis

                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor
                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(session -> session.getCompletedTime().getTime() - session.getStartTime().getTime())
                                    .sum();

                            //bu kısımda 5 saati geçenler datada çok uçlar oluşturduğu için onları çıkarttım datamdan ama sizelarını çıkartmayınca ortalama bu sefer çok küçük geliyor onu yapmadım o da yapılabilir
                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * (60 * 1000));

                        }));

        Map<String, Long> userTotalSensorCount = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<SensorFix> recentSessions = user.getSensorSessions().stream()
                                    .filter(session ->
                                            session.getCompletedTime() != null &&
                                                    session.getCompletedTime().after(oneMonthAgoa)
                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor

                            long sessionCount = recentSessions.size();

                            return sessionCount;

                        }));
        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();

        Map<String, Map<String, Long> > totalSensorChartData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();

        Map<String, Long> userSessionCountsOfLastDay = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(oneDayAgo)
                                )
                                .count()

                ));

        averageTimeChartData.put("averageChartData",userAverageMinLastMonth);

        totalSensorChartData.put("totalSensorChartData",userTotalSensorCount);

        dailydata.put("last_day",userSessionCountsOfLastDay);

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();

        result.add(averageTimeChartData);
        result.add(totalSensorChartData);
        result.add(dailydata);


        return result;
    }

    //last month performance graph for workers
    public         Map<Long, Map<String, Object>>   getSensorSessionsOfLastMonth( Long userId){
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<SensorFix> data = sensorSessionRepository.findLastMonthDataByUserId(userId, oneMonthAgo);
        //sadece completed zamanı ve idsi lazım

        //yada direk id ye göre yapak id completed time şekli
        Map<Long, Map<String, Object>> completedTimeLastMonth = data.stream().collect(Collectors.toMap(
                sensor -> {return  sensor.getId();}
                ,
                sensor -> {

                    Date start = sensor.getStartTime();
                    Date end = sensor.getCompletedTime();

                    long diffMilis = end.getTime() - start.getTime();
                    long diffSeconds = diffMilis / 1000;
                    long diffdays = diffSeconds / (24*3600);

                    diffSeconds %= (24*3600);
                    long hours = diffSeconds / 3600 ;

                    diffSeconds %= 3600;
                    long minutes = diffSeconds /60;

                    long seconds = diffSeconds % 60;

                    Map<String,Object> durationInfo = new HashMap<>();

                    durationInfo.put("rawMinutes", diffMilis / (60 * 1000));
                    durationInfo.put("formatted", String.format("%02d gün %02d saat %02d dakika %02d saniye", diffdays, hours, minutes, seconds));

                    return  durationInfo;
                }
        ));;


        return completedTimeLastMonth;

    }

    public List<SensorCountDTO> getSensorNameCounts(Long userId){
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);

        Map<String,Long> nameCounts = sensorSessions.stream().map(sensor ->


                        sensor.getSensor().getSensorName())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return         nameCounts.entrySet().stream().map(a -> new SensorCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());

    }


    //test etme bakımından vesaire bunun içinin ayrılması daha iiy oluyormuş fonksiyonlara vesaire
    public  List<SensorDateCountDTO> getDatesAndBasedOnTheirSessionCounts(Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);
        //o tarih ve sayılarını almamız lazım
        Map<LocalDate, Long> countsByDate = mapToDateCount(sensorSessions);


        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return convertToSensorDateCountDTO(countsByDate);

    }


    private  Map<LocalDate,Long> mapToDateCount(List<SensorFix> sessions){
        Map<LocalDate,Long> nameCounts = sessions.stream().map(sensor -> sensor.getCompletedTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return nameCounts;
    }
    private List<SensorDateCountDTO> convertToSensorDateCountDTO(Map<LocalDate, Long> counts) {
        return counts.entrySet().stream()
                .map(entry -> new SensorDateCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
    private Map<String, Long> getSinceDateForWorkerStat(Date sinceDate,List<User> users){
        return users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(sinceDate) &&
                                                session.getFinalStatus() == SensorStatus.SOLVED  || session.getFinalStatus() == SensorStatus.ACTIVE)
                                .count()
                ));

    }
    @Transactional
    public    Map<SensorStatus,Long> getSensorStatusesMetricValues(){
        List<SensorFix> sensorSessions = sensorSessionRepository.findAll();

        Map<SensorStatus,Long> statusCounts = sensorSessions.stream().map(sensor -> sensor.getFinalStatus())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return statusCounts;
    }


    @Transactional
    public  List<Map<String, Map<String, Long>>> getWorkerStats(){
        List<User> users = userRepository.findAllByRole(Role.WORKER);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        Date oneWeekAgo = calendar.getTime();

        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgo = calendarOfLastMonth.getTime();


        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();
        //hayatımda böyle bişe görmedim

        Map<String,   Map<String, Long>> weekdata = new HashMap<>();
        Map<String, Map<String, Long> > monthData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();



        Map<String, Long> userSessionCountsOfLastWeeek = getSinceDateForWorkerStat(oneWeekAgo,users);

        Map<String, Long> userSessionCountsOfLastMonth = getSinceDateForWorkerStat(oneMonthAgo,users);

        Map<String, Long> userSessionCountsOfLastDay = getSinceDateForWorkerStat(oneDayAgo,users);

        monthData.put("last_month",userSessionCountsOfLastMonth);

        weekdata.put("last_week",userSessionCountsOfLastWeeek);

        dailydata.put("last_day",userSessionCountsOfLastDay);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);

        return result;
    }
}
