"use client"

import {Flex,Text,VStack,Box} from '@chakra-ui/react'
import { Grid} from '@chakra-ui/react'
import {  useState } from 'react';
import React from 'react'
import GridCalendarHeatmapForWorker from './GridCalendarHeatmapForWorker';
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies';
import { DateTypeCount } from '@/app/supervisor/superVizorDataTypes/types';

const CalendarHeatmapForWorker = ({session,dataForCalendar} : {session : RequestCookie | undefined ,
   dataForCalendar :DateTypeCount[]}) => {




  
    
    const datee = new Date()
    const[currentYear,SetCurrentYear] =useState(datee.getFullYear())
    const[currentMonth,SetCurrenthMonth] =useState(datee.getMonth())

  
    var months = ['January','February','March','April','May','June','July','August','September','October','November','December'];


    return (

         <Flex
      justifyContent={"center"}
    
      className="w-fit h-fit"
  
    >
      <Flex
        border={"1px"}
        borderColor={"#ecebe9"}
        className="w-fit h-fit"
        justifyContent={"center"}
>



        <Flex
        
          justifyContent={"center"}
          className="w-fit h-fit p-[10px]"
        >
          <VStack
          className='w-fit'
            align={"center"}
            gap={"20px"}
          >
            <VStack
              className="h-fit w-fit   rounded-[5px]"
              backgroundColor={"white"}
              gap={"9px"}
            >
              <Text fontSize={"30px"} >
                {months[currentMonth]} {currentYear}{" "}
              </Text>

              <Box className="h-full w-fit">
                <Grid
                  className=" h-fit w-fit gap-[15px] p-[10px] rounded-[20px] max-md:gap-[5px] max-md:repeat(6,1fr)"
                  templateColumns="repeat(7, 1fr) "

                >
                  <GridCalendarHeatmapForWorker dataForCalendar={dataForCalendar}  />
                </Grid>
              </Box>
            </VStack>
          </VStack>
        </Flex>
      </Flex>{" "}
    </Flex>

  )
}

export default CalendarHeatmapForWorker




