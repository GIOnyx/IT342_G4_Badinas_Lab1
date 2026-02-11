import React, { useState, useEffect } from 'react'
import { Routes, Route, Navigate, useLocation, useNavigate } from 'react-router-dom'
import './App.css'
import './styles.css'
import Navbar from './components/Navbar'
import RequireAuth from './components/RequireAuth'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Items from './pages/Items'
import ItemDetails from './pages/ItemDetails'

function App() {
  const [user, setUser] = useState(null)
  const location = useLocation()
  const pathname = location.pathname || ''
  const hideNavbar = pathname === '/' || pathname === '/dashboard' || pathname.startsWith('/login') || pathname.startsWith('/register')
  const navigate = useNavigate()

  async function handleLogout() {
    try {
      await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' })
    } catch (e) {
      // ignore
    }
    setUser(null)
    navigate('/login', { replace: true })
  }

  useEffect(() => {
    // On app load, validate token (HttpOnly cookie) and restore user state
    fetch('/api/auth/validate', { credentials: 'include' })
      .then(async r => {
        if (r.ok) {
          const body = await r.json().catch(() => ({}))
          setUser({ name: body.name, email: body.email })
        } else {
          setUser(null)
        }
      })
      .catch(() => setUser(null))
  }, [])

  return (
    <div id="app-root">
      {!hideNavbar && (
        <Navbar user={user} onLogout={handleLogout} />
      )}
      <main>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/login" element={<Login onLogin={setUser} />} />
          <Route path="/register" element={<Register onRegister={setUser} />} />
          <Route path="/dashboard" element={<RequireAuth><Dashboard user={user} onLogout={handleLogout} /></RequireAuth>} />
          <Route path="/items" element={<RequireAuth><Items /></RequireAuth>} />
          <Route path="/items/:id" element={<RequireAuth><ItemDetails /></RequireAuth>} />
          <Route path="*" element={<h2 style={{padding: '2rem'}}>Page not found</h2>} />
        </Routes>
      </main>
    </div>
  )
}

export default App
