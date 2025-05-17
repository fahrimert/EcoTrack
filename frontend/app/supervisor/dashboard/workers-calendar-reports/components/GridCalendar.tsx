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
import { UserProfile } from '../../components/SensorList'
import { getMonth, getYear } from 'date-fns'
import { useContext, useState } from 'react'
import { DateContext } from '@/context/DateContext'


const GridCalendar = ({userProfile  } : {userProfile: UserProfile }) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
 
    const { date, setDate } = useContext(DateContext);


const currentYear = getYear(new Date()) // Örn: 2025
const currentMonth = getMonth(new Date())  // Örn: 2025
      var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];
      
      function datesForGrid(year, month) {
        var dates= [];

        var firstDay = new Date(year, month).getDay() -1 ;
        var totalDaysInMonth = new Date(year, month + 1, 0).getDate();
        var totalDaysInPrevMonth = new Date(year, month, 0).getDate();


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
            
              <GridItem  w='100%' h='70px' border='1px' borderColor={'#D5D4DF'} >
              <Flex w='100%' h='70px'  alignItems={"center"} justifyContent={"center"} > 
             <Button onClick={() => {setDate(date.date)}}/>
           
          {date.monthClass == 'prev'    || date.monthClass == 'next' ?  <Text  id={date.key}   p='5'  filter='auto' brightness='80%'  bg='white' > {date.date} </Text> :  <Text  id={date.key}    bg='white' > {date.date} </Text> }
          
           </Flex>
        </GridItem>
    
    
            ))  
        

//buradaki amaç her zaman tıklanılabilir olsun tıklandığı zamanı bi stateye atsın 
              
   
  )
}

export default GridCalendar

