import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function RequireAuth({ children }) {
  const [ok, setOk] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    fetch(`/api/auth/validate`, { credentials: 'include' })
    .then(r => {
      if (r.ok) setOk(true)
      else {
        setOk(false)
        navigate('/login', { replace: true })
      }
    })
    .catch(() => {
      setOk(false)
      navigate('/login', { replace: true })
    })
  }, [navigate])

  if (ok === null) return null
  return ok ? children : null
}
