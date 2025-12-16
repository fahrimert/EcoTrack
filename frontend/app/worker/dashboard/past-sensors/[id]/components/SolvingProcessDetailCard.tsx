  import { PastSensorDetailDto } from "@/app/worker/types/types";
import { Card,CardHeader,CardTitle,CardContent } from "@/components/ui/card";
import { Calendar, FileText, Info, User } from "lucide-react";
import { format } from "date-fns";
import { tr } from "date-fns/locale";


const ProcessDetailsCard = ({ data, userProfile }: { data: PastSensorDetailDto, userProfile: UserProfileDTO }) => (
  <Card className="h-full">
    <CardHeader>
      <CardTitle className="text-lg flex items-center gap-2">
        <User size={20} className="text-blue-600" />
        İşlem Kaydı
      </CardTitle>
    </CardHeader>
    <CardContent className="space-y-4">
      <div className="flex items-center gap-3 p-3 bg-slate-50 rounded-lg">
        <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-bold">
            {userProfile.firstName.charAt(0)}
        </div>
        <div>
            <p className="text-sm font-semibold">{userProfile.firstName}</p>
            <p className="text-xs text-slate-500">Müdahale Eden Personel</p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
            <p className="text-xs text-slate-400 mb-1 flex items-center gap-1"><Calendar size={12}/> Başlangıç</p>
            <p className="text-sm font-medium">{format(new Date(data.startTime), "dd MMM HH:mm", { locale: tr })}</p>
        </div>
        <div>
            <p className="text-xs text-slate-400 mb-1 flex items-center gap-1"><Calendar size={12}/> Bitiş</p>
            <p className="text-sm font-medium">{format(new Date(data.completedTime), "dd MMM HH:mm", { locale: tr })}</p>
        </div>
      </div>

      {data.note && (
        <div className="mt-2">
            <p className="text-xs text-slate-400 mb-1 flex items-center gap-1"><FileText size={12}/> Notlar</p>
            <p className="text-sm bg-amber-50 text-amber-900 p-3 rounded-lg border border-amber-100 italic">
                "{data.note}"
            </p>
        </div>
      )}
    </CardContent>
  </Card>
);

export default ProcessDetailsCard
