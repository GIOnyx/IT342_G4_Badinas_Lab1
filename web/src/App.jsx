import { useEffect, useState } from 'react'
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import './App.css'
import './styles.css'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Items from './pages/Items'
import ItemDetails from './pages/ItemDetails'
import RequireAuth from './components/RequireAuth'

function App() {
  const [user, setUser] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    fetch(`/api/auth/validate`, { credentials: 'include' })
      .then(async r => {
        if (r.ok) {
          const body = await r.json().catch(() => null)
          if (body && body.username) {
            setUser({ name: body.username, email: body.username })
          } else if (body && body.email) {
            setUser({ name: body.name || 'User', email: body.email })
          } else {
            setUser({ name: 'User', email: 'unknown' })
          }
        } else {
          setUser(null)
        }
      })
      .catch(() => setUser(null))
  }, [])

  return (
      <div id="app-root">
        <main>
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/login" element={<Login onLogin={setUser} />} />
            <Route path="/register" element={<Register onRegister={setUser} />} />
            <Route path="/dashboard" element={
              <RequireAuth>
                <Dashboard user={user} onLogout={() => { setUser(null); navigate('/login'); }} />
              </RequireAuth>
            } />
            <Route path="/items" element={
              <RequireAuth>
                <Items />
              </RequireAuth>
            } />
            <Route path="/items/:id" element={
              <RequireAuth>
                <ItemDetails />
              </RequireAuth>
            } />
            <Route path="*" element={<h2 style={{padding: '2rem'}}>Page not found</h2>} />
          </Routes>
        </main>
      </div>
  )
}

export default App
