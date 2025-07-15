import { UserOnlineStatusDTO } from "@/app/sharedTypes";


export type  NotificationComponentWrapperNotificationList = {
    sender: UserOnlineStatusDTO | undefined;
    supervizorDescription: string;
    superVizorDeadline: string;
    createdAt: string;
    senderId: number;
    receiverId: number;
    taskId: number;
    isread: boolean;
    id: number;
}[]