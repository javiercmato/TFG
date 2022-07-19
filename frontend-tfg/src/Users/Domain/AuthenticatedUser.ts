import User from "./User";

interface AuthenticatedUser {
    serviceToken: string,
    user: User
}


export default AuthenticatedUser;
