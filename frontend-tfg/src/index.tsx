import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import {store} from './store';
import {Provider} from 'react-redux'
import {App, appRedux} from "./App";
import {initializeBackend} from "./proxy";
import {NetworkErrorException} from "./proxy/exceptions";
import {initReactI18N} from "./i18n";
import {IntlProvider} from "react-intl";
import {ingredientsRedux} from "./Ingredients";
import {recipesRedux} from "./Recipes";


/* Configurar I18N */
const {locale, messages} = initReactI18N();

/* Configurar proxy con el backend: si no establece conexión lanza error */
initializeBackend( () => {
    const networkException: NetworkErrorException = new NetworkErrorException();

    store.dispatch(
        appRedux.actions.error(networkException)
    );
});

/* Acciones a ejecutar tan pronto arranque la aplicación */
store.dispatch(ingredientsRedux.actions.findAllIngredientTypesAsyncAction(() => {}));
store.dispatch(recipesRedux.actions.getCategoriesAsyncAction(() => {}, () => {}));


const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <Provider store={store}>
        <IntlProvider locale={locale} messages={messages}>
            <App />
        </IntlProvider>
    </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
