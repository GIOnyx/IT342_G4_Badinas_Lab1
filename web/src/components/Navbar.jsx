import { NavLink } from 'react-router-dom'
import logo from '../assets/logo.svg'
import ingredientIcon from '../assets/ingredient_icon.png'
import recipeIcon from '../assets/recipe_icon.png'
import favoritesIcon from '../assets/favorites_icon.png'

export default function Navbar({ user, onLogout }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <NavLink to="/ingredients">
          <img src={logo} alt="MiniApp" className="brand-logo" />
          <span>InStock</span>
        </NavLink>
      </div>

      <nav>
        <NavLink to="/ingredients" className={({isActive}) => isActive ? 'active' : ''}>
          <img src={ingredientIcon} alt="" className="nav-icon" />
          <span>Ingredients</span>
        </NavLink>
        <NavLink to="/recipes" className={({isActive}) => isActive ? 'active' : ''}>
          <img src={recipeIcon} alt="" className="nav-icon" />
          <span>Recipes</span>
        </NavLink>
        <NavLink to="/favorites" className={({isActive}) => isActive ? 'active' : ''}>
          <img src={favoritesIcon} alt="" className="nav-icon" />
          <span>Favorites</span>
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        {user ? (
          <>
            <span className="nav-user">{user.name}</span>
            <button onClick={onLogout}>Logout</button>
          </>
        ) : (
          <>
            <NavLink to="/login" className={({isActive}) => isActive ? 'active' : ''}>
              <span>Login</span>
            </NavLink>
            <NavLink to="/register" className={({isActive}) => isActive ? 'active' : ''}>
              <span>Register</span>
            </NavLink>
          </>
        )}
      </div>
    </aside>
  )
}
