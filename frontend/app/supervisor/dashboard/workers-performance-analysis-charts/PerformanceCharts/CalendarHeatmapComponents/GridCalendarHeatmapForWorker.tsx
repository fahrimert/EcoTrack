import { GridItem } from '@chakra-ui/react'
import {Flex,Text} from '@chakra-ui/react'
import { getMonth, getYear } from 'date-fns'
import { DateTypeCount } from '@/app/supervisor/superVizorDataTypes/types'
import { cn } from '@/lib/utils'


const GridCalendarHeatmapForWorker = ({dataForCalendar} : {dataForCalendar : DateTypeCount[]} ) => {
 const calendarDataMap = new Map(
  dataForCalendar.map((item) => [item.date, item.count])
)
function formatDateToYYYYMMDD(date: Date) {
  return date.toISOString().split("T")[0]; 
}


  function getColorByCount(count: number) {
    if (count === 0) return "#f0f0f0";
    if (count < 2) return "#f2dbd3"; // light gray
    if (count < 4) return "#df614c"; // blue
    if (count < 6) return "#f33511"; 
    return "#f33511"; // darkest blue
  }
const currentYear = getYear(new Date()) 
const currentMonth = getMonth(new Date()) 
      var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];
      
      function datesForGrid(year, month) {
        var dates= [];

        var totalDaysInMonth = new Date(year, month + 1, 0).getDate();


          var today = new Date();

             for(var i = 1; i <= totalDaysInMonth; i++) {
                const rawDate = new Date(year, month, i);
      const formattedDate = formatDateToYYYYMMDD(rawDate);
      const count = calendarDataMap.get(formattedDate);

            var key = new Date(year, month, i).toLocaleString();
            if(i === today.getDate() && month === today.getMonth() && year === today.getFullYear() ) {
              dates.push({key: key, date: i, monthClass: 'current', todayClass: 'today'});
            }
            else{ 
              dates.push({key: key, date: i, monthClass: 'current' ,  count : count || 0});
            }
          }  
          
          /* user oluşturulduktan sonra hergün modal olmaya başlicak  */


        return dates;
        }
       

        return (                 
      datesForGrid(currentYear,currentMonth).map((date) => (  
            
              <GridItem  w='100%'   className='h-fit w-fit rounded-[10px] '>
              <Flex   alignItems={"center"} justifyContent={"center"}    className='flex flex-col w-fit h-fit p-[10px] max-md:p-[0]  rounded-[10px] '         bg={getColorByCount(date.count)}
 > 


          {date.monthClass == 'prev'    || date.monthClass == 'next' ?  <Text  id={date.key}   p='5'  filter='auto' brightness='80%'   > {date.date} </Text> :  <Text  id={date.key} className={cn(`${date.count === 0 ? "text-[#e55e3f]" : "text-white"  }  `)}    >  {date.date} {months[currentMonth]} </Text> }
           <h2 className={cn(`${date.count === 0 ? "text-[#e55e3f] text-[14px]" : "text-white"  } text-[14px]  `)}>Onarılma sayısı-{date.count}</h2>
          
           </Flex>
        </GridItem>
    
    
            ))  
        

//buradaki amaç her zaman tıklanılabilir olsun tıklandığı zamanı bi stateye atsın 
              
   
  )
}

export default GridCalendarHeatmapForWorker

