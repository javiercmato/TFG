interface User {
    userID?: string,
    nickname?: string,
    password?: string,
    name?: string,
    surname?: string,
    email?: string,
    avatar?: string,
    registerdate?: Date,
    role?: string,
    isBannedByAdmin?: boolean
}

export default User;
