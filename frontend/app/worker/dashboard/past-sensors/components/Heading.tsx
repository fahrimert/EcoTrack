import { Separator } from "@/components/ui/separator";

interface HeadingProps {
  title: String;
  description: Date;
}

const Heading: React.FC<HeadingProps> = ({ title, description }) => {
  return (
    <>
<div className="flex flex-col gap-2">
      <h2 className="text-3xl font-bold tracking-tight text-slate-900">{title}</h2>
      <p className="text-sm text-slate-500">{description}</p>
      <Separator className="mt-4 bg-slate-200" />
    </div>
    </>
  );
};

export default Heading;
