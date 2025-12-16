"use client"

import { Button } from "@/components/ui/button";
import { Form, FormControl,  FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import {  useForm } from "react-hook-form";
import {z} from "zod";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import Image from "next/image";
import { Textarea } from "@/components/ui/textarea";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import toast from "react-hot-toast";
import { updateSensorForWorker } from "@/app/actions/sensorActions/updateSensorForWorker";
import { SensorStatus, SensorStatusConfig } from "@/enums/enums";
import { SensorSolvingSensorDto } from "@/app/worker/types/types";

import { Loader2, UploadCloud, Info } from "lucide-react"; 
import { Badge } from "@/components/ui/badge";


const formSchema = z.object({
  not: z.string().min(10, { message: "Açıklama en az 10 karakter olmalıdır." }), 
  statusId: z.string().min(1, { message: "Lütfen bir durum seçiniz." }),
  files: z.any().optional(), 
});
export type AssignedSensorFormValues = z.infer<typeof formSchema>


const AssignedSensorForm= ({initialData} : {initialData: SensorSolvingSensorDto }) => {
    const [loading,setLoading] = useState(false)
const form = useForm<AssignedSensorFormValues>({
    resolver: zodResolver(formSchema),
    mode: "onChange",
    defaultValues: {
      not: initialData.currentSensorSession?.note || "",
      statusId: initialData.status || "",
    },
  });

  
const onSubmit = async (data: AssignedSensorFormValues) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("note", data.not);
      formData.append("statusID", data.statusId);
      
      if (data.files && data.files.length > 0) {
        for (let i = 0; i < data.files.length; i++) {
          formData.append("files", data.files[i]);
        }
      }

      const returnData = await updateSensorForWorker(formData, initialData.id); 
      
      if (returnData.serverData) {
          toast.success("Güncelleme başarılı!");
      } else {
          toast.error("Bir hata oluştu.");
      }

    } catch (error) {
      console.error(error);
      toast.error("Beklenmeyen bir hata oluştu.");
    } finally {
      setLoading(false);
    }
  };

return (
    <div className="w-full h-full p-4 md:p-0 space-y-6">
      
      <Card className="w-full border-0 shadow-sm bg-white overflow-hidden">
        <div className="relative h-48 w-full bg-slate-100">
           <Image
              src="/indir.jpg"
              alt="Sensor Image"
              fill
              className="object-cover"
           />
           <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
           <div className="absolute bottom-4 left-4 text-white">
              <h2 className="text-2xl font-bold">{initialData.sensorName}</h2>
           {/*    <p className="text-sm opacity-90 flex items-center gap-2">
                 <Info size={16} /> 
                 Arıza Kaydı: {new Date(initialData.currentSensorSession!.startTime).toLocaleDateString("tr-TR")}
              </p> */}
           </div>
        </div>
        
        <CardContent className="pt-6">
           <div className="flex flex-col gap-2">
              <h3 className="font-semibold text-lg flex items-center gap-2">
                 Arıza Detayı
                 <Badge variant="outline" className="text-xs font-normal text-slate-500">Otomatik Tespit</Badge>
              </h3>
              <p className="text-sm text-slate-600 leading-relaxed bg-slate-50 p-4 rounded-xl border border-slate-100">
                 Sensör verilerinde ani voltaj düşüklüğü tespit edildi. Kablo bağlantılarının ve güç ünitesinin kontrol edilmesi gerekmektedir.
              </p>
           </div>
        </CardContent>
      </Card>

      <Card className="border-0 shadow-md">
        <CardHeader>
           <CardTitle className="text-xl">Müdahale Raporu</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              
              <FormField
                control={form.control}
                name="not"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Yapılan İşlemler / Notlar</FormLabel>
                    <FormControl>
                      <Textarea 
                        disabled={loading} 
                        placeholder="Örn: Kablo değişimi yapıldı, sistem yeniden başlatıldı..." 
                        className="min-h-[120px] resize-none focus-visible:ring-emerald-500"
                        {...field} 
                      />
                    </FormControl>
<FormMessage className="text-red-500 font-medium" />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="statusId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Yeni Durum</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value} disabled={loading}>
                      <FormControl>
                        <SelectTrigger className="h-12">
                          <SelectValue placeholder="Durum Seçiniz" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {Object.values(SensorStatus).map((status) => (
                          <SelectItem key={status} value={status}>
                            <div className="flex items-center gap-2">
                              <span 
                                className="w-2 h-2 rounded-full" 
                                style={{ backgroundColor: SensorStatusConfig[status]?.color || "#ccc" }} 
                              />
                              {SensorStatusConfig[status]?.label || status}
                            </div>
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
<FormMessage className="text-red-500 font-medium" />                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="files"
                render={({ field: { onChange, value, ...rest } }) => (
                  <FormItem>
                    <FormLabel>Fotoğraf Kanıtı (Opsiyonel)</FormLabel>
                    <FormControl>
                      <div className="flex items-center justify-center w-full">
                        <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed border-slate-300 rounded-xl cursor-pointer bg-slate-50 hover:bg-slate-100 transition-colors">
                          <div className="flex flex-col items-center justify-center pt-5 pb-6">
                            <UploadCloud className="w-8 h-8 text-slate-400 mb-2" />
                            <p className="text-sm text-slate-500 font-medium">Fotoğraf yüklemek için tıklayın</p>
                            <p className="text-xs text-slate-400">JPG, PNG (Max 5MB)</p>
                          </div>
                          <Input
                            {...rest}
                            type="file"
                            multiple
                            className="hidden"
                            onChange={(e) => onChange(e.target.files)}
                          />
                        </label>
                      </div>
                    </FormControl>
                    {value && value.length > 0 && (
                        <p className="text-xs text-emerald-600 font-medium mt-1">
                            {value.length} dosya seçildi.
                        </p>
                    )}
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Button 
                type="submit" 
                disabled={loading} 
                className="w-full h-12 bg-black hover:bg-slate-800 text-white font-medium rounded-xl transition-all"
              >
                {loading ? (
                    <>
                        <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                        Güncelleniyor...
                    </>
                ) : (
                    "Kaydı Tamamla"
                )}
              </Button>

            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};

export default AssignedSensorForm