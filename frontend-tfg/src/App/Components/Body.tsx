import {Route, Routes} from 'react-router-dom';
import {Container} from "react-bootstrap";
import AppGlobalComponents from "./AppGlobalComponents";
import {
    ChangePassword,
    Login,
    Logout,
    PrivateListsPage,
    SignUp,
    UpdateProfile,
    UserProfile,
    userRedux
} from "../../Users";
import {body} from './styles/body';
import {useAppSelector} from "../../store";
import {IngredientsPage} from "../../Ingredients";
import {CreateRecipeForm, RecipeDetails, RecipesPage} from "../../Recipes";
import {FollowersPage} from "../../Social";


const Body = () => {
    const isUserLogged = useAppSelector(userRedux.selectors.isLoggedIn);

    return (
        <Container className={Body.name} style={body}>
            <br/>
            <AppGlobalComponents />
            <Routes>
                <Route path="/" element={<RecipesPage />} />
                {/* ****************************** USERS ****************************** */}
                <Route path="/signUp" element={<SignUp />} />
                <Route path="/login" element={<Login />} />
                <Route path="/users/:userID" element={<UserProfile />} />
                {isUserLogged && <Route path="/logout" element={<Logout />} />}
                {isUserLogged && <Route path="/changePassword" element={<ChangePassword />} />}
                {isUserLogged && <Route path="/profile" element={<UpdateProfile />} />}
                {isUserLogged && <Route path="/lists" element={<PrivateListsPage />} />}
                {/* ****************************** INGREDIENTS ****************************** */}
                <Route path="/ingredients" element={<IngredientsPage />} />
                {/* ****************************** RECIPES ****************************** */}
                <Route path="/recipes" element={<RecipesPage />} />
                <Route path="/recipes/create" element={<CreateRecipeForm />} />
                <Route path="/recipes/:recipeID" element={<RecipeDetails />} />
                {/* ****************************** SOCIAL ****************************** */}
                <Route path="/users/:userID/followers" element={<FollowersPage />} />


                {/* ****************************** DEFAULT ROUTE ****************************** */}
                <Route element={<RecipesPage />} />
            </Routes>
        </Container>
    )
}


export default Body;
