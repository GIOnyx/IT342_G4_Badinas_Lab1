import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Register({ onRegister }) {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  function handleSubmit(e) {
    e.preventDefault()
    const user = { id: Date.now(), name: name || email.split('@')[0], email }
    onRegister(user)
    navigate('/dashboard')
  }

  return (
    <div className="page container">
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
    </div>
  )
}
