import { Separator } from "@/components/ui/separator";

interface HeadingProps {
  title: String;
  description: String;
}

const Heading: React.FC<HeadingProps> = ({ title, description }) => {
  return (
    <>
      <div className="relative w-full  h-fit flex flex-col justify-center items-center gap-0 p-[20px] rounded-[15px]  
  bg-[#1d1e22]
  ">
        <div className="relative w-full h-fit flex flex-col justify-center items-center  px-[10px]">
          <div className="relative w-full h-fit flex flex-row  justify-center items-center   p-0">
            <h2 className=" relative w-full h-fit  text-[24px]  text-white " >
              {title}
            </h2>
          </div>

          <div className="w-full h-fit py-[15px] ">
            <h2 className="w-full h-full text-[16px] text-white  items-start">
            {description}
            </h2>
          </div>
        </div>
      </div>
    </>
  );
};

export default Heading;
