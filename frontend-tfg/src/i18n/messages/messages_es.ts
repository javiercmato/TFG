const translations = {
    /* ******************** proxy ******************** */
    'proxy.exceptions.Error': 'Error inesperado',
    'proxy.exceptions.NetworkErrorException': 'Fallo en la conexión con el servidor',
    'proxy.exceptions.ServiceException': 'Error en el servicio',


    /* ******************** common ******************** */
    'common.results': 'Resultados',


    'common.buttons.open': 'Abrir',
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
    'common.buttons.delete': 'Borrar',
    'common.buttons.publish': 'Publicar',




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
    'common.fields.privateLists': 'Listas privadas',
    'common.fields.comments': 'Comentarios',


    'common.Components.WarningModal.title': 'Aviso',
    'common.alerts.noResults': 'Sin resultados',
    'common.alerts.listWithoutItems': 'Esta lista no tiene ningún elemento',
    'common.alerts.notLoggedIn': 'No puedes realizar esta operación sin estar registrado',

    /* ******************** App ******************** */
    'app.components.ErrorDialog.title' : 'Error',
    'app.components.Header.signUp': 'Registrar',
    'app.components.Header.login': 'Acceder',
    'app.components.Header.globalLinks.ingredients': 'Ingredientes',
    'app.components.Header.globalLinks.recipes': 'Recetas',
    'app.components.Header.userActions.logout': 'Cerrar sesión',
    'app.components.Header.userActions.changePassword': 'Cambiar contraseña',
    'app.components.Header.userActions.seeProfile': 'Ver mi perfil',
    'app.components.Header.userActions.editProfile': 'Editar perfil',
    'app.components.Header.userActions.privateLists': 'Mis listas privadas',


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
    'users.components.UserProfile.deleteUser': 'Borrar usuario',
    'users.components.PrivateListsPage.title': 'Crear lista privada',
    'users.components.PrivateListsPage.myLists': 'Mis listas privadas',
    'users.components.CreatePrivateList.title.placeholder': 'P. ej.: "Veranito ☀️"',
    'users.components.CreatePrivateList.description.placeholder': 'P. ej.: "Recetas para el verano"',
    'users.components.CreatePrivateList.createListButton': 'Crear lista privada',


    'users.warning.UserIsBannedByAdmin': 'Usuario ha sido moderado por el administrador',


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
    'recipes.components.CreateRecipeButton.title': 'Crear receta',
    'recipes.components.FindRecipesForm.searchButton': 'Buscar recetas',
    'recipes.components.RecipeCard.button.recipeDetails': 'Ver receta',
    'recipes.components.RecipeCard.button.removeFromList': 'Retirar de lista privada',
    'recipes.components.BanRecipeButton.banButton': 'Inhabillitar receta',
    'recipes.components.BanRecipeButton.unbanButton': 'Rehabillitar receta',
    'recipes.warning.RecipeIsBannedByAdmin': 'Receta ha sido moderada por el administrador',
    'recipes.components.AddToPrivateListButton.text': 'Añadir a lista privada',
    'recipes.components.CommentInput.placeholder': 'P. ej.: ¡Me gusta mucho tu receta!',
    'recipes.components.CommentBox.banButton': 'Inhabillitar comentario',
    'recipes.components.CommentBox.unbanButton': 'Rehabilitar comentario',
    'recipes.warning.CommentIsBannedByAdmin': 'Comentario ha sido moderado por el administrador',



}

export default translations;
