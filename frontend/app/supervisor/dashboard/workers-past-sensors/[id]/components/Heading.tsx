
interface HeadingProps {
  title: String;
  description: String;
}

const Heading: React.FC<HeadingProps> = ({ title, description }) => {
  return (
    <>
      <div className="relative w-full  h-fit flex flex-col justify-center items-center gap-0 p-[20px] rounded-[15px] bg-[#f1f0ee] 
bg-gradient-to-b from-[#f1f0ee] to-[#e6e5e3]
shadow-[inset_-1px_0_0_rgba(0,0,0,0.1),inset_1px_0_0_rgba(255,255,255,0.3)]
  t-[10px] pl-[5px] ">
        <div className="relative w-full h-fit flex flex-col justify-center items-center  px-[10px]">
          <div className="relative w-full h-fit flex flex-row  justify-center items-center   p-0">
            <h2 className=" relative w-full h-fit  text-[36px]  text-black " >
              {title}
            </h2>
          </div>
          <div className="w-full h-[40px] ">
            <h2 className="w-full h-full text-[16px] text-black  items-start">
            {description}
            </h2>
          </div>
          <div className="w-full h-[3px]  bg-gray-300" />
        </div>
      </div>
    </>
  );
};

export default Heading;
