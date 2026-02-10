import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'

export default function Register({ onRegister }) {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    // call backend register (use dev proxy)
    fetch(`/api/auth/register`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    })
    .then(async r => {
      const body = await r.json().catch(() => ({}))
      if (r.ok) {
        const user = { name: body.name, email: body.email }
        onRegister(user)
        navigate('/dashboard')
      } else {
        const msg = body.message || (body.errors ? JSON.stringify(body.errors) : 'Registration failed')
        alert(msg)
      }
    })
    .catch(() => alert('Registration error'))
  }

  return (
    <div className="auth-page">
      <div className="auth-content">
        <div className="auth-side">
          <h1 className="brand">MiniApp</h1>
          <p className="lead">Create an account to save your items and access the dashboard.</p>
        </div>
        <div className="auth-card">
          <h2>Register</h2>
          <form className="form" onSubmit={handleSubmit}>
            <label>Name
              <input value={name} onChange={e => setName(e.target.value)} required />
            </label>
            <label>Email
              <input value={email} onChange={e => setEmail(e.target.value)} type="email" required />
            </label>
            <label>Password
              <input value={password} onChange={e => setPassword(e.target.value)} type="password" required />
            </label>
            <button type="submit">Create account</button>
          </form>
          <div className="auth-switch">Already have an account? <Link to="/login">Login</Link></div>
        </div>
      </div>
    </div>
  )
}
