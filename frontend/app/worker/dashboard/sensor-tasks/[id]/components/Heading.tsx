import { format } from "date-fns";
import { tr } from "date-fns/locale";

interface HeadingProps {
  title: String;
  description: string;
  taskSender:string
  superVizorDescription:string
}

const Heading: React.FC<HeadingProps> = ({ title, description , taskSender ,superVizorDescription }) => {
  return (
    <>
      <div className="w-full h-full flex flex-col gap-8 bg-white  p-[20px] justify-center transition-all duration-300 rounded-[30px]  ">
        <div className="relative w-full h-fit flex flex-col justify-center items-center  px-[10px]">
          <div className="relative w-full h-fit flex flex-row  justify-center items-center   p-0">
            <h2 className=" relative w-full h-fit  text-[36px]  text-black " >
              {title}
            </h2>
          </div>
          <div className="flex flex-col gap-[5p] h-fit w-full">
          <div className="w-full h-fit ">
            <h2 className="w-full h-full text-[16px] text-black  items-start">
             Süpervizor Tarafından Verilen Açıklama : {superVizorDescription}.
            </h2>
         
          </div>
          <div className="w-full h-fit ">
            <h2 className="w-full h-full text-[16px] text-black  items-start">
             Göreve Verilen Deadline : {
                        format(new Date(description), 'dd MMMM yyyy HH:mm')

             }
            </h2>
         
          </div>
          
          <div className="w-full h-fit ">
            <h2 className="w-full h-full text-[16px] text-black  items-start">
             Görevi Veren Kişi  : {taskSender}
            </h2>
         
          </div>
          </div>

        </div>
      </div>
    </>
  );
};

export default Heading;
