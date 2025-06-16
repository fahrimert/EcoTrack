
import { ChartComponent } from "./GraphComponents/ChartComponent";
import { cn } from "@/lib/utils";
import BarChart from "./GraphComponents/LineChart";
import HeatMap from "./GraphComponents/HeatMap";
import OnlineUsers from "./OnlineUserListComponents/OnlineUsers";
import { FullUserStats, statusEnums} from "../../superVizorDataTypes/types";


const MainPageChartsComponent = async({responseOfDoughbut,responseOfBarChart,responseOHeatmapComponent} : {
  responseOfDoughbut :statusEnums,
responseOfBarChart : FullUserStats 
responseOHeatmapComponent:
 [{
    id: string;
    latitude: number;
    longitude: number;
}]
}

) => {

const transformed = responseOfBarChart.map((periodObj) => {
  const [periodKey, periodValue] = Object.entries(periodObj)[0];
  return {
    period: periodKey,
    labels: Object.keys(periodValue),
    data: Object.values(periodValue),
  };
});


const lastweekdata = transformed.filter((c) => c.period == 'last_week')
const lastmonthdata = transformed.filter((c) => c.period == 'last_month')
const lastdaydata = transformed.filter((c) => c.period == 'last_day')


       
  return (
    <>
      <div className={cn(`relative w-full h-fit flex flex-col  items-center  justify-center  gap-[24px] p-0 `)}  >
       
  
          <div className="relative w-[60%] h-fit flex flex-col justify-between items-center  bg-[#050505] p-[20px] gap-[20px] rounded-[30px] ">
            <h2 className="w-full h-fit flex justify-start items-center text-white text-[24px]  border-b-[1px]">Tüm sensörlerin Status Grafikleri </h2>


            <div className="w-full h-fit flex flex-col items-center justify-center" >
            <div className=" w-full h-fit">
              <h2 className="text-white"> Geçmişteki İşçilerin Görev Harici Çözdükleri Sensörlerin Durumlarına  Göre Grafiği</h2>

            </div>
            <div className="w-full h-fit flex p-[20px] items-center justify-center ">
            {responseOfDoughbut !== null ? 
               <div className=" w-full h-[300px] flex flex-row items-center justify-center ">

                 <ChartComponent responseOfDoughbut={responseOfDoughbut} />
               </div>
               
               : 
               <h2 className="w-full h-full flex justify-center items-center text-white rounded-[30px] bg-[#14213f]"> Henüz Bir Veri YOK</h2> }
           
            </div>
            </div>
         
            
          
          </div>
  
   <div className=" bg-white w-full h-full flex justify-center items-center rounded-[20px] p-[20px] ">
         <BarChart 
         lastmonthdata = {lastmonthdata}
         lastdaydata = {lastdaydata}
        lastweekdata = {lastweekdata}
        />
     </div> 
              
              <div className=" w-[70%] h-fit bg-white rounded-[20px] p-[20px]">
                <HeatMap response = {responseOHeatmapComponent}/>
              </div>

              <div  className=" w-fit h-fit bg-white rounded-[20px] p-[20px] ">
    <h2 className='text-[24px]'>Aktif İşçilerin Listesi</h2>
              <OnlineUsers/>
              </div>
        </div> 
    </>
  );
};

export default MainPageChartsComponent;
