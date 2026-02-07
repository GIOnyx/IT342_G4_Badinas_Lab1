import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Register({ onRegister }) {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:3060'
    // call backend register
    fetch(`${API_URL}/api/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    })
    .then(async r => {
      const body = await r.json().catch(() => ({}))
      if (r.ok && body.token) {
        const user = { name: body.name, email: body.email, token: body.token }
        localStorage.setItem('token', body.token)
        onRegister(user)
        navigate('/ingredients')
      } else {
        const msg = body.message || (body.errors ? JSON.stringify(body.errors) : 'Registration failed')
        alert(msg)
      }
    })
    .catch(() => alert('Registration error'))
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2>Create Account</h2>
        <form className="form" onSubmit={handleSubmit}>
          <label>Name
            <input value={name} onChange={e => setName(e.target.value)} placeholder="Your name" required />
          </label>
          <label>Email
            <input value={email} onChange={e => setEmail(e.target.value)} type="email" placeholder="you@example.com" required />
          </label>
          <label>Password
            <input value={password} onChange={e => setPassword(e.target.value)} type="password" placeholder="••••••••" required />
          </label>
          <button type="submit">Create account</button>
        </form>
        <div className="auth-switch">Already have an account? <a href="/login">Sign in</a></div>
      </div>
    </div>
  )
}
