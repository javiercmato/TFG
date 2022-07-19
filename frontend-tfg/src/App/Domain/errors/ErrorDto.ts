import {FieldErrorDto} from "./FieldErrorDto";

type ErrorDto = {
    globalError : string,
    fieldErrors? : FieldErrorDto[]
}

export type {ErrorDto};
