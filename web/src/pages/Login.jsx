import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    // mock login
    const user = { id: 1, name: email.split('@')[0] || 'User', email }
    onLogin(user)
    navigate('/dashboard')
  }

  return (
    <div className="page container">
      <h2>Login</h2>
      <form className="form" onSubmit={handleSubmit}>
        <label>Email
          <input value={email} onChange={e => setEmail(e.target.value)} type="email" required />
        </label>
        <label>Password
          <input value={password} onChange={e => setPassword(e.target.value)} type="password" required />
        </label>
        <button type="submit">Sign in</button>
      </form>
    </div>
  )
}
