import { useContext } from "react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { HeatMapSelectWorkerContext } from "@/context/HeatMapSelectWorkerContext";
import { UserOnlineStatusDTO } from "@/app/sharedTypes";


const WorkerSelectDifferentForHeatmap = ({users,label} : {
    users:UserOnlineStatusDTO[],
    label:string
}) => {
      const { usernameForHeatmap, setUsernameForHeatmap } = useContext(HeatMapSelectWorkerContext);
  return (
    <div className="bg-white border-black border-[1px] w-[200px] p-[10px] gap-[5px] rounded-[10px]">
    <Select onValueChange={(e) => setUsernameForHeatmap({username:Number(e)})} value={String(usernameForHeatmap)}>
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

export default WorkerSelectDifferentForHeatmap