import { GridItem } from '@chakra-ui/react'
import {Flex,Text,Button} from '@chakra-ui/react'
import { useDisclosure } from '@chakra-ui/react'
import { getMonth, getYear } from 'date-fns'
import { useContext, useState } from 'react'
import { DateContext } from '@/context/DateContext'


const GridCalendar = () => {
  const { isOpen, onOpen, onClose } = useDisclosure()
 
    const { date, setDate } = useContext(DateContext);


const currentYear = getYear(new Date()) // Örn: 2025
const currentMonth = getMonth(new Date())  // Örn: 2025
      
      function datesForGrid(year, month) {
        var dates= [];

        var totalDaysInMonth = new Date(year, month + 1, 0).getDate();


          var today = new Date();
          for(var i = 1; i <= totalDaysInMonth; i++) {
            var key = new Date(year, month, i).toLocaleString();
            if(i === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
              dates.push({key: key, date: i, monthClass: 'current', todayClass: 'today'});
            }
            else{ 
              dates.push({key: key, date: i, monthClass: 'current'});
            }
          }  
          
          /* user oluşturulduktan sonra hergün modal olmaya başlicak  */


        return dates;
        }
       

        return (                 
      datesForGrid(currentYear,currentMonth).map((date) => (  
            
              <GridItem  w='100%' h='70px' border='1px' borderColor={'#efe9ff'} bg={'#f3f5f7'} className=' hover:bg-[#efe9ff]  hover:text-[#7438e5] duration-150 hover:inset-2 hover:ring-2 hover:ring-[#efe9ff]' >
              <Button onClick={() => {setDate(date.date)}} className='w-full h-full flex items-center justify-center '>
              <Flex w='100%' className=' h-full w-full'  alignItems={"center"} justifyContent={"center"} > 
           
          {date.monthClass == 'prev'    || date.monthClass == 'next' ?  <Text  id={date.key}   p='5'  filter='auto' brightness='80%'   className='text-[16px]' > {date.date} </Text> :  <Text  id={date.key}    className='text-[16px]' > {date.date} </Text> }
          
           </Flex>
             </Button>

        </GridItem>
    
    
            ))  
        

//buradaki amaç her zaman tıklanılabilir olsun tıklandığı zamanı bi stateye atsın 
              
   
  )
}

export default GridCalendar

