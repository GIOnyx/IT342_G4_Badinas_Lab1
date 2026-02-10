import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import './App.css'
import './styles.css'
import Navbar from './components/Navbar'
import Landing from './pages/Landing'
import Login from './pages/Login'
import Register from './pages/Register'
import Items from './pages/Items'
import ItemDetails from './pages/ItemDetails'
import Recipes from './pages/Recipes'
import Ingredients from './pages/Ingredients'
import Favorites from './pages/Favorites'

function AuthenticatedRoutes({ user, onLogout }) {
  const navigate = useNavigate()

  const handleLogout = () => {
    onLogout()
    navigate('/')
  }

  return (
    <>
      <Navbar user={user} onLogout={handleLogout} />
      <div className="main-content">
        <Routes>
          <Route path="/items" element={<Items />} />
          <Route path="/items/:id" element={<ItemDetails />} />
          <Route path="/ingredients" element={<Ingredients />} />
          <Route path="/recipes" element={<Recipes />} />
          <Route path="/favorites" element={<Favorites />} />
          <Route path="*" element={<h2 style={{padding: '2rem'}}>Page not found</h2>} />
        </Routes>
      </div>
    </>
  )
}

function App() {
  const [user, setUser] = useState(null)

  // Load user from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser))
      } catch (err) {
        console.error('Failed to parse stored user:', err)
        localStorage.removeItem('user')
      }
    }
  }, [])

  // Save user to localStorage whenever it changes
  const handleSetUser = (userData) => {
    if (userData) {
      localStorage.setItem('user', JSON.stringify(userData))
      setUser(userData)
    } else {
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      setUser(null)
    }
  }

  return (
    <BrowserRouter>
      <div id="app-root">
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login onLogin={handleSetUser} />} />
          <Route path="/register" element={<Register onRegister={handleSetUser} />} />
          <Route path="/*" element={<AuthenticatedRoutes user={user} onLogout={handleSetUser} />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
