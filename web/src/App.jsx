import { useState } from 'react'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import './styles.css'
import Navbar from './components/Navbar'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Items from './pages/Items'
import ItemDetails from './pages/ItemDetails'

function App() {
  const [user, setUser] = useState(null)

  return (
    <BrowserRouter>
      <div id="app-root">
        <Navbar user={user} onLogout={() => setUser(null)} />
        <main>
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/login" element={<Login onLogin={setUser} />} />
            <Route path="/register" element={<Register onRegister={setUser} />} />
            <Route path="/dashboard" element={<Dashboard user={user} />} />
            <Route path="/items" element={<Items />} />
            <Route path="/items/:id" element={<ItemDetails />} />
            <Route path="*" element={<h2 style={{padding: '2rem'}}>Page not found</h2>} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  )
}

export default App
