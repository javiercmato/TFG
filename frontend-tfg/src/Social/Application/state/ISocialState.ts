import {Follow} from "../../Domain";


interface ISocialState {
    followings: Array<Follow>,               // Usuarios a los que sigue el usuario actual
    followers: Array<Follow>,               // Seguidores del usuario actual
}

const initialState: ISocialState = {
    followings: [],
    followers: [],
}

export type {ISocialState};
export {initialState};
