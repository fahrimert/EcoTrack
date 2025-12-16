  import { PastSensorDetailDto } from "@/app/worker/types/types";
import { Card,CardHeader,CardTitle,CardContent } from "@/components/ui/card";
import { Info } from "lucide-react";
const SensorDetailsCard = ({ data }: { data: PastSensorDetailDto }) => (
  <Card className="h-full">
    <CardHeader>
      <CardTitle className="text-lg flex items-center gap-2">
        <Info size={20} className="text-emerald-600" />
        Genel Detaylar
      </CardTitle>
    </CardHeader>
    <CardContent className="space-y-4">
      <div className="flex justify-between py-2 border-b border-slate-100">
        <span className="text-slate-500 text-sm">Sensör Adı</span>
        <span className="font-medium text-sm">{data.sensorName}</span>
      </div>
      <div className="flex justify-between py-2 border-b border-slate-100">
        <span className="text-slate-500 text-sm">Son Durum</span>
        <span className="font-medium text-sm">{data.finalStatus}</span>
      </div>
      <div className="flex justify-between py-2 border-b border-slate-100">
        <span className="text-slate-500 text-sm">Sensör ID</span>
        <span className="font-medium text-sm font-mono">{data.sensorId}</span>
      </div>
    </CardContent>
  </Card>
);

export default SensorDetailsCard
