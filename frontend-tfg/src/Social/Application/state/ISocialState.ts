import {Follow, Notification} from "../../Domain";
import {Search} from "../../../App";
import defaultSearch from "../../../App/Domain/common/Search";


interface ISocialState {
    followings: Search<Follow>,               // Usuarios a los que sigue el usuario actual
    followers: Search<Follow>,               // Seguidores del usuario actual
    notifications: Search<Notification>,     // Notificaciones del usuario actual
}

const initialState: ISocialState = {
    followings: defaultSearch,
    followers: defaultSearch,
    notifications: defaultSearch,
}

export type {ISocialState};
export {initialState};
