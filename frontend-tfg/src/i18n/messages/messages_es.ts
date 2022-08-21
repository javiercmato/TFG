const translations = {
    /* ******************** proxy ******************** */
    'proxy.exceptions.Error': 'Error inesperado',
    'proxy.exceptions.NetworkErrorException': 'Fallo en la conexión con el servidor',
    'proxy.exceptions.ServiceException': 'Error en el servicio',


    /* ******************** common ******************** */
    'common.results': 'Resultados',


    'common.buttons.close': 'Cerrar',
    'common.buttons.edit': 'Editar',
    'common.buttons.uploadFile': 'Subir fichero',
    'common.buttons.create': 'Crear',
    'common.buttons.search': 'Buscar',
    'common.buttons.clear': 'Limpiar',
    'common.buttons.previous': 'Anterior',
    'common.buttons.next': 'Siguiente',
    'common.buttons.add': 'Añadir',
    'common.buttons.remove': 'Retirar',



    'common.validation.mandatoryField': 'Campo obligatorio',
    'common.validation.email': 'Introduzca un correo electrónico válido',
    'common.validation.passwordsDoNotMatch': 'Contraseñas no coinciden',


    'common.fields.name': 'Nombre',
    'common.fields.surname': 'Apellidos',
    'common.fields.email': 'Email',
    'common.fields.nickname': 'Nombre de usuario',
    'common.fields.password': 'Contraseña',
    'common.fields.confirmPassword': 'Confirmar contraseña',
    'common.fields.avatar': 'Foto de perfil',
    'common.fields.ingredientType': 'Tipo de ingrediente',
    'common.fields.category': 'Categoría',
    'common.fields.measureUnit': 'Unidad de medida',
    'common.fields.description': 'Descripción',
    'common.fields.ingredients': 'Ingredientes',
    'common.fields.ingredient': 'Ingrediente',
    'common.fields.quantity': 'Cantidad',
    'common.fields.pictures': 'Imágenes',
    'common.fields.steps': 'Pasos',
    'common.fields.minutes': 'minutos',
    'common.fields.diners': 'raciones',


    'common.alerts.noResults': 'Sin resultados',
    'common.alerts.notLoggedIn': 'No puedes realizar esta operación sin estar registrado',

    /* ******************** App ******************** */
    'app.components.ErrorDialog.title' : 'Error',
    'app.components.Header.signUp': 'Registrar',
    'app.components.Header.login': 'Acceder',
    'app.components.Header.globalLinks.ingredients': 'Ingredientes',
    'app.components.Header.globalLinks.recipes': 'Recetas',
    'app.components.Header.userActions.logout': 'Cerrar sesión',
    'app.components.Header.userActions.changePassword': 'Cambiar contraseña',
    'app.components.Header.userActions.seeProfile': 'Ver perfil',
    'app.components.Header.userActions.editProfile': 'Editar perfil',


    /* ******************** Users ******************** */
    'users.components.SignUp.title': 'Registrar usuario',
    'users.components.SignUp.signUpButton': 'Registrarse',
    'users.componentes.SignUp.confirmPasswordError': 'Revisa que las contraseñas coincidan',
    'users.components.Login.title': 'Iniciar sesión',
    'users.components.Login.loginButton': 'Acceder',
    'users.components.Login.signUpEncouragementText': '¿Todavía no tienes cuenta?',
    'users.components.ChangePassword.title': 'Cambiar contraseña',
    'users.components.ChangePassword.oldPassword': 'Antigua contraseña',
    'users.components.ChangePassword.newPassword': 'Nueva contraseña',
    'users.components.ChangePassword.confirmNewPassword': 'Repite la nueva contraseña',
    'users.components.ChangePassword.changePassword': 'Cambiar contraseña',
    'users.components.UpdateProfile.title': 'Actualizar perfil',
    'users.components.UpdateProfile.updateButton': 'Actualizar perfil',
    'users.components.BanUserButton.banButton': 'Inhabillitar usuario',
    'users.components.BanUserButton.unbanButton': 'Rehabillitar usuario',
    'users.components.UserProfile.deleteUser': 'Delete user',
    'users.warning.UserIsBannedByAdmin': 'User has been banned by administrator',


    /* ******************** Ingredients ******************** */
    'ingredients.components.CreateIngredientType.title': 'Crear nuevo tipo de ingrediente',
    'ingredients.components.CreateIngredientType.placeholder': 'P. ej.: "Verdura"',
    'ingredients.components.CreateIngredient.title': 'Crear nuevo ingrediente',
    'ingredients.components.CreateIngredient.placeholder': 'P. ej.: "Zanahoria"',
    'ingredients.components.FindIngredients.findIngredients': 'Buscar ingredientes',


    /* ******************** Recipes ******************** */
    'recipes.components.CreateCategory.title': 'Crear nueva categoría',
    'recipes.components.CreateCategory.placeholder': 'P. ej.: "Cócteles"',
    'recipes.components.CreateRecipeForm.title': 'Crear nueva receta',
    'recipes.components.CreateRecipeForm.title.placeholder': 'P. ej.: "Macarrones con tomate y atún"',
    'recipes.components.CreateRecipeForm.description.placeholder': 'P. ej.: "Pláto rápido y sencillo de preparar, fácil de llevar a cualquier sitio"',
    'recipes.components.CreateRecipeForm.duration.title': 'Duración (minutos)',
    'recipes.components.CreateRecipeForm.duration.placeholder': '15',
    'recipes.components.CreateRecipeForm.diners.title': 'Raciones',
    'recipes.components.CreateRecipeForm.diners.placeholder': '2',
    'recipes.components.AddIngredientToRecipeModal.title': 'Añadir ingrediente a receta',
    'recipes.components.AddIngredientToRecipeModal.quantity.placeholder': 'P. ej.: 200',
    'recipes.components.RecipeIngredientsSelection.title': 'Ingredientes seleccionados',
    'recipes.components.CreateStepsForm.title': 'Pasos de preparación:',
    'recipes.components.CreateStepsForm.placeholder': 'P. ej.: Poner a hervir en una olla el agua con sal',


}

export default translations;
