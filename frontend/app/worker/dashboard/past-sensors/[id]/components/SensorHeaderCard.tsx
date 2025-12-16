  import { PastSensorDetailDto } from "@/app/worker/types/types";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import Image from "next/image";


const SensorHeaderCard = ({ data }: { data: PastSensorDetailDto }) => (
  <Card className="overflow-hidden border-0 shadow-md">
    <div className="relative h-64 w-full bg-slate-100">
      {data.iconImage?.base64Image ? (
        <Image
          src={`data:image/png;base64,${data.iconImage.base64Image}`}
          alt={data.sensorName}
          fill
          className="object-cover"
        />
      ) : (
        <div className="flex items-center justify-center h-full text-slate-400">Görsel Yok</div>
      )}
      <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent" />
      <div className="absolute bottom-6 left-6 text-white">
        <h1 className="text-3xl font-bold">{data.sensorName}</h1>
        <div className="flex items-center gap-3 mt-2">
          <Badge variant={data.finalStatus === "SOLVED" ? "default" : "secondary"}>
            {data.finalStatus || "Durum Belirsiz"}
          </Badge>
          <span className="text-sm opacity-80">ID: {data.sensorId}</span>
        </div>
      </div>
    </div>
  </Card>
);

export default SensorHeaderCard
