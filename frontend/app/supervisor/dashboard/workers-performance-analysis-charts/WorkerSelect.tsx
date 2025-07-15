import { UserOnlineStatusDTO } from "@/app/sharedTypes";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";


const WorkerSelect = ({users,value,onChange,label} : {
    users:UserOnlineStatusDTO[],
    value:number,
    onChange: (value:number) => void
    label:string
}) => {
  return (
    <div className="bg-white border-black border-[1px] w-[200px] p-[10px] gap-[5px] rounded-[10px]">
    <Select onValueChange={(e) => onChange(Number(e))} value={String(value)}>
      <h2 className=" text-black mb-[10px]">{label}</h2>
      <SelectTrigger className="text-black">
        <SelectValue placeholder={`${label} seçiniz`} />
      </SelectTrigger>
      <SelectContent className="bg-white">
        {users.map((user) => (
          <SelectItem key={user.id} value={String(user.id)} className="text-black">
            {user.firstName}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  </div>
  )
}

export default WorkerSelect