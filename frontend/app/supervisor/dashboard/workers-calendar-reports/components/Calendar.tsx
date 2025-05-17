"use client"

import {Flex,Text,VStack,Box,Button,Skeleton} from '@chakra-ui/react'
import { Grid, GridItem } from '@chakra-ui/react'
import {ArrowForwardIcon,ArrowBackIcon} from '@chakra-ui/icons'
import { useEffect, useState } from 'react';

import { VscPerson } from "react-icons/vsc";
import { LuClock } from 'react-icons/lu';
import React from 'react'
import GridCalendar from './GridCalendar';
import axios from 'axios';
import { UserProfile } from '../../components/SensorList';
import { SensorSessionWithser } from '../../workers-past-sensors/components/WorkerPastSensorWrapper';
import { DateContext } from '@/context/DateContext';

const Calendar = ({session} : {session : string | undefined}) => {
  //yapcağımız ne son 5 ini değil aslında tarihi bugünkü olanları default olarak gösterme
  //tıkladığında ise o tarihe ait olanları gösterme 
  const [userProfile,setUserProfile] = useState<UserProfile>()

  useEffect(() => {
    axios.get(`http://localhost:8080/user/profile/${session}`, {
      headers: { Authorization: `Bearer ${session}` },
      withCredentials: true,
    })
    .then((res) => setUserProfile(res.data))
    .catch((err) => console.log(err));
  }, []);

  
    
    const datee = new Date()
    const[currentYear,SetCurrentYear] =useState(datee.getFullYear())
    const[currentMonth,SetCurrenthMonth] =useState(datee.getMonth())

  
    var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];


    return (

      <Flex  justifyContent={'center'}  w={{md:'100%',base:'100%'}}  className='h-fit'  backgroundColor={'#202024'} >
      <Flex w={{md:'container.md',base:'100%'}}  border={'1px'} borderColor={'#7A6868'} className='h-fit'  justifyContent={'center'} >
    <Flex w={{md:'container.md',base:'100%'}} justifyContent={"center"}   className='h-fit p-[20px]'  backgroundColor={"#202024"} >
 <VStack  w={{md:'container.md',base:'50%'}} align={"center"}     gap={"20px"}>
    <Flex    className='h-fit'  alignItems={"center"} justifyContent={"center"} >
    <Flex w={{md:'100%', base:'100%'}} className='h-fit'  alignItems={"center"} justifyContent={"center"}  > 
    
    <VscPerson color='white' size={'20px'}/>
    <Text color={"white"}> Welcome {userProfile?.firstName}  </Text></Flex>
    </Flex>
    <VStack className='h-fit w-full' backgroundColor={"white"}  gap={"9px"}>
        
         <Text fontSize={'30px'}> 
         {months[currentMonth]} {currentYear} </Text>
     
         
        <Box className='h-full w-full'>
          <Grid  className=' h-fit w-full'  templateColumns='repeat(7, 1fr)'  > 
         
                <GridCalendar  
                  userProfile = {userProfile}
                monthState = {currentMonth}
                              yearState = {currentYear}/>
             </Grid>
        </Box>
    </VStack>


      </VStack >
    </Flex>
    </Flex>     </Flex>

  )
}

export default Calendar




