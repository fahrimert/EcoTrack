import { Card,CardHeader,CardTitle,CardContent } from "@/components/ui/card";
import { Camera } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogTrigger,
} from "@/components/ui/dialog"
import Image from "next/image";
const SolvingProcessEvidenceGallery = ({ images }: { images: any[] }) => {
    if (!images || images.length === 0) return null;

    return (
        <Card>
            <CardHeader>
                <CardTitle className="text-lg flex items-center gap-2">
                    <Camera size={20} className="text-purple-600" />
                    Kanıt Fotoğrafları ({images.length})
                </CardTitle>
            </CardHeader>
            <CardContent>
                <div className="flex flex-wrap gap-4">
                    {images.slice(0, 4).map((img, index) => (
                        <Dialog key={index}>
                            <DialogTrigger asChild>
                                <div className="relative w-24 h-24 rounded-lg overflow-hidden cursor-pointer hover:opacity-80 transition-opacity border border-slate-200">
                                    <Image 
                                        src={`data:image/png;base64,${img.base64Image}`} 
                                        alt="Evidence" 
                                        fill 
                                        className="object-cover" 
                                    />
                                </div>
                            </DialogTrigger>
                            <DialogContent className="max-w-3xl bg-transparent border-0 shadow-none p-0">
                                <div className="relative w-full aspect-video rounded-xl overflow-hidden bg-black">
                                     <Image 
                                        src={`data:image/png;base64,${img.base64Image}`} 
                                        alt="Evidence Full" 
                                        fill 
                                        className="object-contain" 
                                    />
                                </div>
                            </DialogContent>
                        </Dialog>
                    ))}
                    {images.length > 4 && (
                        <div className="w-24 h-24 flex items-center justify-center bg-slate-100 rounded-lg text-slate-500 font-medium text-sm">
                            +{images.length - 4}
                        </div>
                    )}
                </div>
            </CardContent>
        </Card>
    );
}

export default SolvingProcessEvidenceGallery
