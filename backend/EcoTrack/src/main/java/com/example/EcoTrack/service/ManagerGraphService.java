package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.SensorCountDTO;
import com.example.EcoTrack.dto.TaskCountDTO;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.TaskRepository;
import com.example.EcoTrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ManagerGraphService {
    public UserRepository userRepository;
    public TaskRepository taskRepository;
    public  UserService userService;

    public ManagerGraphService(UserRepository userRepository,TaskRepository taskRepository,UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }
    //burda yapmam gereken şey supervizorlerin yani userların ki görev verebilenler de sadece supervizorler
    //ama bz sadece supervizorleri queryleyip onlarda da verdikleri tasklarda workerarrivedi boş olmayanların sayısını alıcaz isimleriyle beraber

    private Map<String, Long> getSinceDateForSuperVizorTaskStat(Date sinceDate, List<User> users){
        return users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getTasksIAssigned().stream()
                                .filter(task->
                                        task.getWorkerArrived() != null &&
                                                task.getTaskCompletedTime().after(sinceDate) &&
                                                task.getWorkerArrived() == true  )
                                .count()
                ));

    }

    //en son tam burda kaldım 2. grafiği çıkartmaya çalışıyorum o da scatter plot grafiği
    //bu grafikte mevzu ortalama dakkalarını alıp workerların hangisi daha çok task bitirmiş ona bakmak
    public  Map<String,   Map<String, Long>> getTheAverageTaskCompletedTimeForWorkerChart(){
        List<User> users = userRepository.findAllByRole(Role.WORKER);

        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();


        Map<String, Long> userAverageMinLastMonth = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<Task> recentSessions = user.getTasksAssignedToMe().stream()
                                    .filter(task->
                                            task.getTaskCompletedTime() != null &&

                                                    task.getWorkerArrived() == true &&
                                                    task.getTaskCompletedTime().after(oneMonthAgoa)
                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor
                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(task ->
                                            {
                                                Instant completedTtaskInstant = task.getTaskCompletedTime().toInstant();
                                                Instant createdInstant = task.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant

                                                return Duration.between(createdInstant,completedTtaskInstant).toMillis();
                                            })
                                    .sum();

                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * 60 * 1000);

                        }));


        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();



        averageTimeChartData.put("averageChartData",userAverageMinLastMonth);



        return averageTimeChartData;
    }
    @Transactional
    public List<Map<String, Map<String, Long>>> getSuperVizorTasks(){
        List<User> superVizors = userRepository.findAllByRole(Role.SUPERVISOR);
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



        Map<String, Long> userSessionCountsOfLastWeeek = getSinceDateForSuperVizorTaskStat(oneWeekAgo,superVizors);

        Map<String, Long> userSessionCountsOfLastMonth = getSinceDateForSuperVizorTaskStat(oneMonthAgo,superVizors);

        Map<String, Long> userSessionCountsOfLastDay = getSinceDateForSuperVizorTaskStat(oneDayAgo,superVizors);

        monthData.put("last_month",userSessionCountsOfLastMonth);

        weekdata.put("last_week",userSessionCountsOfLastWeeek);

        dailydata.put("last_day",userSessionCountsOfLastDay);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);

        return result;
    }


    public List<TaskCountDTO> getSensorNameCounts(){
        List<Task> supervizorTasks = taskRepository.findAll();

        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup,,

        Map<SensorStatus,Long> statusCounts = supervizorTasks.stream().map(task->
                        task.getFinalStatus())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return    statusCounts.entrySet().stream().map(a -> new TaskCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());

    }

    public  List<Map<String, Map<String, Long>>> getThePerformanceTableForSupervizor(){
        List<User> users = userRepository.findAllByRole(Role.SUPERVISOR);


        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();

        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        long maxAcceptableDurationMillis = 5L  * 60 * 60 * 1000;

        Map<String, Long> superVizorAverageMinOfTasksLastMonth = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<Task> recentSessions = user.getTasksIAssigned().stream()
                                    .filter(session ->

                                            {
                                                if (session.getTaskCompletedTime() == null) return false;

                                                Instant completedTtaskInstant = session.getTaskCompletedTime().toInstant();
                                                Instant createdInstant = session.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant

                                                return session.getTaskCompletedTime() != null &&
                                                        session.getTaskCompletedTime().after(oneMonthAgoa) &&
                                            Duration.between(createdInstant,completedTtaskInstant).toMillis() <= maxAcceptableDurationMillis;


                                            }

                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor
                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(task ->
                                            {
                                        Instant completedTtaskInstant = task.getTaskCompletedTime().toInstant();
                                        Instant createdInstant = task.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant

                                        return  Duration.between(createdInstant,completedTtaskInstant).toMillis();

                                    })

                                    .sum();

                            //bu kısımda 5 saati geçenler datada çok uçlar oluşturduğu için onları çıkarttım datamdan ama sizelarını çıkartmayınca ortalama bu sefer çok küçük geliyor onu yapmadım o da yapılabilir
                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * (60 * 1000));

                        }));

        Map<String, Long> superVizorGivenTasksTotalCount = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<Task> recentSessions = user.getTasksIAssigned().stream()
                                    .filter(session ->
                                            session.getTaskCompletedTime() != null &&
                                                    session.getTaskCompletedTime().after(oneMonthAgoa)
                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor

                            long sessionCount = recentSessions.size();

                            return sessionCount;

                        }));
        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();

        Map<String, Map<String, Long> > totalSensorChartData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();

        Map<String, Long> superVizorGivenTaskCountOfLastDay = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getTasksIAssigned().stream()
                                .filter(task ->
                                        task.getTaskCompletedTime() != null &&
                                                task.getTaskCompletedTime().after(oneDayAgo)
                                )
                                .count()

                ));

        averageTimeChartData.put("SuperVizorGivenCompletedTasksAverageMinLastMonth",superVizorAverageMinOfTasksLastMonth);

        totalSensorChartData.put("SupervizorGivenTasksTotalCount",superVizorGivenTasksTotalCount);

        dailydata.put("SupervizorGivenTaskCountOfLastDay",superVizorGivenTaskCountOfLastDay);

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();

        result.add(averageTimeChartData);
        result.add(totalSensorChartData);
        result.add(dailydata);


        return result;
    }



    public List<Map<String, Long>>  getSuperVizorPropertiesForRadarChart(Long userId){
        User user = userService.findById(userId);

            long countOfTheCompletedTasksAfterDeadline = user.getTasksIAssigned().stream()
                .filter(session ->
                        {
                            Date completedTime = session.getTaskCompletedTime();
                            LocalDateTime deadline = session.getSuperVizorDeadline();

                            if (completedTime == null || deadline == null) return  false;

                            Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant
                            Date deadlineDate = Date.from(deadlineInstant);
                            return  completedTime.after(deadlineDate);
                        }).count();

            long totalTaskNumber = user.getTasksIAssigned().stream().filter(a -> a.getTaskCompletedTime() != null).collect(Collectors.toList()).size();

        long takeTheRatioOfAfterDeadline = Math.round((double) countOfTheCompletedTasksAfterDeadline / totalTaskNumber * 100);

        long countOfTheCompletedTasksBeforeDeadline = user.getTasksIAssigned().stream()
                .filter(session ->
                {
                    Date completedTime = session.getTaskCompletedTime();
                    LocalDateTime deadline = session.getSuperVizorDeadline();

                    if (completedTime == null || deadline == null) return  false;

                    Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant
                    Date deadlineDate = Date.from(deadlineInstant);
                    return  completedTime.after(deadlineDate);
                }).count();


        long takeTheRatioOfBeforeDeadline = Math.round((double) countOfTheCompletedTasksBeforeDeadline / totalTaskNumber * 100);



        long countOfTheActiveTasks = user.getTasksIAssigned().stream()
                .filter(session -> session.getFinalStatus() == SensorStatus.ACTIVE)
                .count();
        long takeTheRatioOfFinishedStatusActiveTasks = Math.round((double) countOfTheActiveTasks / totalTaskNumber * 100);



        long countOfTheFinishedTasks= user.getTasksIAssigned().stream()
                .filter(session -> session.getTaskCompletedTime() != null )
                .count();
        long takeTheRatioOfFinishedTasks = Math.round((double) countOfTheFinishedTasks / totalTaskNumber * 100);


        Map<String,  Long> takeTheRatioOfAfterDeadlineHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfFinishedStatusActiveTasksHashmap = new HashMap<>();
        Map<String, Long > takeTheRatioOfBeforeDeadlineTasksHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfFinishedTasksHashmap = new HashMap<>();



        takeTheRatioOfAfterDeadlineHashmap.put("Given Tasks Solved After Deadline Ratio",takeTheRatioOfAfterDeadline);


        takeTheRatioOfBeforeDeadlineTasksHashmap.put("Given Tasks Solved Before Deadline Ratio",takeTheRatioOfBeforeDeadline);

        takeTheRatioOfFinishedStatusActiveTasksHashmap.put("Given Tasks Final Status Actıve Ratio",takeTheRatioOfFinishedStatusActiveTasks);

        takeTheRatioOfFinishedTasksHashmap.put("Given Tasks Finished Task Ratıo",takeTheRatioOfFinishedTasks);


        List<Map<String, Long>> result = new ArrayList<>();

        result.add(takeTheRatioOfAfterDeadlineHashmap);
        result.add(takeTheRatioOfBeforeDeadlineTasksHashmap);
        result.add(takeTheRatioOfFinishedStatusActiveTasksHashmap);
        result.add(takeTheRatioOfFinishedTasksHashmap);


        return result;
    }



    public List<Map<String, Long>>  getWorkerPropertiesForRadarChart(Long userId){
        User user = userService.findById(userId);

        long countOfTheCompletedTasksAfterDeadlineForWorker = user.getTasksAssignedToMe().stream()
                .filter(session ->
                {
                    Date completedTime = session.getTaskCompletedTime();
                    LocalDateTime deadline = session.getSuperVizorDeadline();

                    if (completedTime == null || deadline == null) return  false;

                    Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant
                    Date deadlineDate = Date.from(deadlineInstant);
                    return  completedTime.after(deadlineDate);
                }).count();

        long totalTaskNumber = user.getTasksAssignedToMe().stream().filter(a -> a.getTaskCompletedTime() != null).collect(Collectors.toList()).size();
        long totalSessionNumber = user.getSensorSessions().stream().filter(a -> a.getCompletedTime() != null).collect(Collectors.toList()).size();


        long takeTheRatioOfAfterDeadlineForWorker = Math.round((double) countOfTheCompletedTasksAfterDeadlineForWorker / totalTaskNumber * 100);

        long countOfTheCompletedTasksBeforeDeadline = user.getTasksAssignedToMe().stream()
                .filter(session ->
                {
                    Date completedTime = session.getTaskCompletedTime();
                    LocalDateTime deadline = session.getSuperVizorDeadline();

                    if (completedTime == null || deadline == null) return  false;

                    Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant
                    Date deadlineDate = Date.from(deadlineInstant);
                    return  completedTime.before(deadlineDate);
                }).count();


        long takeTheRatioOfBeforeDeadline = Math.round((double) countOfTheCompletedTasksBeforeDeadline / totalTaskNumber * 100);



        long countOfTheActiveTasks = user.getTasksAssignedToMe().stream()
                .filter(session -> session.getFinalStatus() == SensorStatus.ACTIVE)
                .count();
        long countOfTheActivePastSessions = user.getSensorSessions().stream()
                .filter(session -> session.getFinalStatus() == SensorStatus.ACTIVE)
                .count();

        long takeTheRatioOfFinishedTaskAndSessionStatusActiveTasksWorker = Math.round((double) (countOfTheActiveTasks + countOfTheActivePastSessions ) / (totalTaskNumber + totalSessionNumber) * 100);




        long countOfTheNotActiveNorSolvedTasks = user.getTasksAssignedToMe().stream()
                .filter(session -> session.getFinalStatus() != SensorStatus.ACTIVE && session.getFinalStatus() != SensorStatus.SOLVED)
                .count();
        long countOfTheNotActiveNorSolvedTasksSessions = user.getSensorSessions().stream()
                .filter(session -> session.getFinalStatus()  != SensorStatus.ACTIVE && session.getFinalStatus() != SensorStatus.SOLVED)
                .count();

        long takeTheRatioOfUnFinishedTaskAndSessionStatusActiveTasksWorker = Math.round((double) (countOfTheNotActiveNorSolvedTasks + countOfTheNotActiveNorSolvedTasksSessions ) / (totalTaskNumber + totalSessionNumber) * 100);




        long countOfTheFinishedTasks= user.getTasksAssignedToMe().stream()
                .filter(session -> session.getTaskCompletedTime() != null )
                .count();
        long takeTheRatioOfFinishedTasks = Math.round((double) countOfTheFinishedTasks / totalTaskNumber * 100);


        Map<String,  Long> takeTheRatioOfAfterDeadlineForWorkerHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfFinishedTaskAndSessionStatusActiveTasksWorkerHashmap = new HashMap<>();
        Map<String, Long > takeTheRatioOfBeforeDeadlineTasksHashmap = new HashMap<>();
        Map<String, Long > takeTheRatioOfFinishedTasksTasksHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfUnFinishedTaskAndSessionStatusActiveTasksWorkerHashmap = new HashMap<>();



        takeTheRatioOfAfterDeadlineForWorkerHashmap.put("Given Tasks Solved After Deadline Ratio For Worker" ,takeTheRatioOfAfterDeadlineForWorker);


        takeTheRatioOfBeforeDeadlineTasksHashmap.put("Given Tasks Solved Before Deadline Ratio For Worker",takeTheRatioOfBeforeDeadline);

        takeTheRatioOfFinishedTaskAndSessionStatusActiveTasksWorkerHashmap.put("Given Tasks And Session Final Status Actıve Ratio For Worker",takeTheRatioOfFinishedTaskAndSessionStatusActiveTasksWorker);

        takeTheRatioOfFinishedTasksTasksHashmap.put("Given Tasks Finished Ratio For Worker",takeTheRatioOfFinishedTasks);

        takeTheRatioOfUnFinishedTaskAndSessionStatusActiveTasksWorkerHashmap.put("Given Tasks And Session Final Status Not Actıve Ratio For Worker",takeTheRatioOfUnFinishedTaskAndSessionStatusActiveTasksWorker);


        List<Map<String, Long>> result = new ArrayList<>();

        result.add(takeTheRatioOfAfterDeadlineForWorkerHashmap);
        result.add(takeTheRatioOfBeforeDeadlineTasksHashmap);
        result.add(takeTheRatioOfFinishedTasksTasksHashmap);

        result.add(takeTheRatioOfFinishedTaskAndSessionStatusActiveTasksWorkerHashmap);
        result.add(takeTheRatioOfUnFinishedTaskAndSessionStatusActiveTasksWorkerHashmap);


        return result;
    }
}
