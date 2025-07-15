"use client";

import { Flex, Text, VStack, Box } from "@chakra-ui/react";
import { Grid } from "@chakra-ui/react";
import { useState } from "react";
import React from "react";
import GridCalendar from "./GridCalendar";
import { useUserProfile } from "@/hooks/useUserProfile";
import { RequestCookie } from "next/dist/compiled/@edge-runtime/cookies";
import Heading from "./Heading";

const Calendar = ({ session }: { session: RequestCookie | undefined }) => {
  const { userProfile, loading, error } = useUserProfile(session);

  const datee = new Date();
  const [currentYear, SetCurrentYear] = useState(datee.getFullYear());
  const [currentMonth, SetCurrenthMonth] = useState(datee.getMonth());

  var months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  return (
    <Flex
      justifyContent={"center"}
      w={{ md: "100%", base: "100%" }}
      className="h-fit"
  
    >
      <Flex
        w={{ md: "container.md", base: "100%" }}
        border={"1px"}
        borderColor={"#ecebe9"}
        className="h-fit"
        justifyContent={"center"}
>



        <Flex
          w={{ md: "container.md", base: "100%" }}
          justifyContent={"center"}
          className="h-fit p-[10px]"
        >
          <VStack
            w={{ md: "container.md", base: "100%" }}
            align={"center"}
            gap={"20px"}
          >
            <Flex
              className="h-fit"
              alignItems={"center"}
              justifyContent={"center"}
            >
              <Flex
                w={{ md: "100%", base: "100%" }}
                className="h-fit flex flex-col"
                alignItems={"center"}
                justifyContent={"center"}
              >

                <Heading
                title={`Pdf Raporlarına Hoşgeldiniz ${userProfile?.firstName}`}
                description={"Buradan ilgili tarihin butonuna tıklayarak o tarihteki işçilerin çözdükleri sensör raporlarını görüp analiz veya rapor için pdfini alabilirsiniz."}
                />
              </Flex>
            </Flex>
            <VStack
              className="h-fit w-full ring-[10px] ring-[#57427f] border-[#57427f] ring-opacity-50 rounded-[5px]"
              backgroundColor={"white"}
              gap={"9px"}
            >
              <Text fontSize={"30px"} >
                {months[currentMonth]} {currentYear}{" "}
              </Text>

              <Box className="h-full w-full">
                <Grid
                  className=" h-fit w-full gap-[5px] p-[5px] rounded-[20px]"
                  templateColumns="repeat(7, 1fr)"

                >
                  <GridCalendar />
                </Grid>
              </Box>
            </VStack>
          </VStack>
        </Flex>
      </Flex>{" "}
    </Flex>
  );
};

export default Calendar;
