"use client";
import React, { useEffect, useState } from "react";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Badge } from "@/components/ui/badge";
import { IoCheckbox, IoFileTrayOutline, IoNotifications, IoTimeOutline } from "react-icons/io5";
import { ScrollArea } from "@/components/ui/scroll-area";
import { format, formatDistanceToNow } from "date-fns";
import Link from "next/link";
import { tr } from "date-fns/locale";
import { EnrichedNotification } from "@/app/sharedTypes";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface NotificationComponentProps {
  notifications: EnrichedNotification[];
  unreadCount: number;
  onMarkAsRead: () => void;
}

const NotificationComponent = ({
  notifications,
  unreadCount,
  onMarkAsRead,
}: NotificationComponentProps) => {

  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);


if (!isMounted) {
    return null; 
  }

 return (
    <div className="flex justify-end items-center mt-4 mr-4">
      <Popover>
        <PopoverTrigger asChild onClick={onMarkAsRead}>
          <Button
            variant="ghost"
            size="icon"
            className="relative rounded-full w-10 h-10 hover:bg-gray-100 transition-all duration-200"
          >
            <IoNotifications size={22} className={cn("text-gray-600", unreadCount > 0 && "text-gray-800")} />
            
            {unreadCount > 0 && (
              <span className="absolute top-2 right-2 flex h-3 w-3">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500 border-2 border-white"></span>
              </span>
            )}
          </Button>
        </PopoverTrigger>

        <PopoverContent className="w-[380px] p-0 mr-4 shadow-xl border-slate-100" align="end">
          <div className="flex items-center justify-between p-4 bg-white/50 backdrop-blur-sm border-b border-gray-100">
            <div className="flex items-center gap-2">
              <h2 className="font-semibold text-base text-gray-800">Bildirimler</h2>
              {unreadCount > 0 && (
                <span className="bg-red-100 text-red-600 text-[10px] font-bold px-2 py-0.5 rounded-full">
                  {unreadCount} Yeni
                </span>
              )}
            </div>
            <div className="text-xs text-gray-400 flex items-center gap-1">
                <IoCheckbox />
                <span>Otomatik Okundu</span>
            </div>
          </div>

          <ScrollArea className="h-[450px]">
            {notifications.length === 0 ? (
              <div className="flex flex-col items-center justify-center h-[300px] text-gray-400 gap-3">
                <div className="bg-gray-50 p-4 rounded-full">
                    <IoFileTrayOutline size={40} className="text-gray-300" />
                </div>
                <p className="text-sm font-medium text-gray-500">Henüz bildiriminiz yok.</p>
                <p className="text-xs text-gray-400">Görev atandığında burada görünecek.</p>
              </div>
            ) : (
              <div className="flex flex-col">
                {notifications.map((notif) => {
                  const isTaskActive = new Date(notif.superVizorDeadline).getTime() > Date.now();
                  const senderInitial = notif.sender?.firstName?.charAt(0) || "S";

                  return (
                    <div
                      key={notif.id}
                      className={cn(
                        "group relative flex gap-4 p-4 border-b border-gray-50 hover:bg-gray-50/80 transition-all duration-200",
                        !notif.isread ? "bg-blue-50/30" : "bg-white"
                      )}
                    >
                      {!notif.isread && (
                        <div className="absolute left-0 top-0 bottom-0 w-[3px] bg-blue-500 rounded-r-full" />
                      )}

                      <div className="flex-shrink-0 mt-1">
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-gray-100 to-gray-200 border border-gray-300 flex items-center justify-center text-gray-600 font-bold shadow-sm">
                          {senderInitial}
                        </div>
                      </div>

                      <div className="flex-1 flex flex-col gap-1.5">
                        <div className="flex justify-between items-start">
                            <span className="text-sm font-semibold text-gray-900">
                                {notif.sender?.firstName || "Sistem"}
                            </span>
                            <span className="text-[10px] text-gray-400 flex items-center gap-1">
                                <IoTimeOutline />
                                {formatDistanceToNow(new Date(notif.createdAt), { addSuffix: true, locale: tr })}
                            </span>
                        </div>

                        <p className="text-sm text-gray-600 leading-relaxed">
                          {notif.supervizorDescription}
                        </p>

                        <div className="flex justify-between items-center mt-2">
                           <div className="text-[11px] font-medium text-gray-400 bg-gray-100 px-2 py-1 rounded-md">
                               Son: {formatDistanceToNow(new Date(notif.superVizorDeadline), { addSuffix: true, locale: tr })}
                           </div>

                           {isTaskActive && (
                            <Link href={`/worker/dashboard/tasks/${notif.taskId}`}>
                                <Button size="sm" variant="outline" className="h-7 text-xs px-3 border-gray-300 hover:border-black hover:bg-black hover:text-white transition-colors">
                                    Göreve Git
                                </Button>
                            </Link>
                           )}
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </ScrollArea>
          
          {notifications.length > 0 && (
             <div className="p-2 bg-gray-50 text-center border-t border-gray-100">
                <button className="text-xs text-gray-500 hover:text-black font-medium transition-colors">
                    Geçmiş bildirimleri gör
                </button>
             </div>
          )}
        </PopoverContent>
      </Popover>
    </div>
  );
};

export default NotificationComponent;
