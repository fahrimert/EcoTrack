import { GridItem } from '@chakra-ui/react'
import {Flex,Text,Button,HStack,Textarea ,Checkbox} from '@chakra-ui/react'
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper,
} from '@chakra-ui/react'
import { useDisclosure } from '@chakra-ui/react'
import { getMonth, getYear } from 'date-fns'
import { useContext, useState } from 'react'
import { DateContext } from '@/context/DateContext'
import { DateTypeCount } from './ChartComponent'


const GridCalendarHeatmapForWorker = ({dataForCalendar} : {dataForCalendar : DateTypeCount[]} ) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
 const calendarDataMap = new Map(
  dataForCalendar.map((item) => [item.date, item.count])
)
function formatDateToYYYYMMDD(date: Date) {
  return date.toISOString().split("T")[0]; // "2025-05-14"
}
    const { date, setDate } = useContext(DateContext);


  function getColorByCount(count: number) {
    if (count === 0) return "white";
    if (count < 2) return "#edf2f7"; // light gray
    if (count < 4) return "#90cdf4"; // blue
    if (count < 6) return "#3182ce"; // darker blue
    return "#2b6cb0"; // darkest blue
  }
const currentYear = getYear(new Date()) // Örn: 2025
const currentMonth = getMonth(new Date())  // Örn: 2025
      var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];
      
      function datesForGrid(year, month) {
        var dates= [];

        var firstDay = new Date(year, month).getDay() -1 ;
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
            
              <GridItem  w='100%'  border='1px' borderColor={'#D5D4DF'}  className='h-fit'>
              <Flex w='100%'   alignItems={"center"} justifyContent={"center"}    className='flex flex-col w-fit h-fit'         bg={getColorByCount(date.count)}
 > 


           <h2>Onarılma sayısı: {date.count}</h2>
          {date.monthClass == 'prev'    || date.monthClass == 'next' ?  <Text  id={date.key}   p='5'  filter='auto' brightness='80%'   > Tarih:{date.date} </Text> :  <Text  id={date.key} className='text-black'    > Tarih: {date.date} {months[currentMonth]} </Text> }
          
           </Flex>
        </GridItem>
    
    
            ))  
        

//buradaki amaç her zaman tıklanılabilir olsun tıklandığı zamanı bi stateye atsın 
              
   
  )
}

export default GridCalendarHeatmapForWorker

