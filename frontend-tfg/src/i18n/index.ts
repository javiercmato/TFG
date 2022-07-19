import messages from "./messages";

/** Traduce los mensajes al Locale del navegador */
export const initReactI18N = () => {
    // Comprobar si navegador tiene un idioma principal por defecto
    let hasDefaultLocale = navigator.language;
    // Comprobar si navegador tiene idiomas configurados
    let hasDefaultLocales = navigator.languages && navigator.languages[0];
    // Idioma por defecto
    const defaultLocale = 'es';

    let locale = hasDefaultLocales || hasDefaultLocale || defaultLocale;

    // Retira la región del locale (se queda con el país)
    // Locales son de la forma 'es_ES', 'es_MX' (idioma_REGION)
    const localeWithoutRegionCode = locale.toLowerCase().split(/[_-]+/)[0];

    // @ts-ignore
    const localeMessages = messages[locale] || messages[localeWithoutRegionCode] || messages[defaultLocale];

    // Intenta devolver los mensajes en el locale indicado
    locale = (localeMessages === messages[defaultLocale]) ? defaultLocale : locale;

    return {locale, messages: localeMessages}
}
