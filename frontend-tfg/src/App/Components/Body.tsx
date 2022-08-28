import {Route, Routes} from 'react-router-dom';
import {Container} from "react-bootstrap";
import AppGlobalComponents from "./AppGlobalComponents";
import Home from "./Home";
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


const Body = () => {
    const isUserLogged = useAppSelector(userRedux.selectors.isLoggedIn);

    return (
        <Container className={Body.name} style={body}>
            <br/>
            <AppGlobalComponents />
            <Routes>
                <Route path="/" element={<Home />} />
                {/* ****************************** USERS ****************************** */}
                <Route path="/signUp" element={<SignUp />} />
                <Route path="/login" element={<Login />} />
                <Route path="/users/:nickname" element={<UserProfile />} />
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


                {/* ****************************** DEFAULT ROUTE ****************************** */}
                <Route element={<Home />} />
            </Routes>
        </Container>
    )
}


export default Body;
