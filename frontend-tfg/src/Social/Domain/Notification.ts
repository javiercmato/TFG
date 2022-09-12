interface Notification {
    id: string,
    isRead: boolean,
    createdAt: Date,
    title: string,
    message: string,
}

export default Notification;
