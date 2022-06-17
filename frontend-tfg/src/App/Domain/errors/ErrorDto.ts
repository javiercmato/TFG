import {FieldErrorDto} from "./FieldErrorDto";

interface ErrorDto {
    globalError : string,
    fieldErrors? : FieldErrorDto[]
}

export type {ErrorDto};
