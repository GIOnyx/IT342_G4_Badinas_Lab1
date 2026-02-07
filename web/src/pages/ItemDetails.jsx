import { useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import api from '../api'

export default function ItemDetails() {
  const { id } = useParams()
  const [item, setItem] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api(`/api/items/${id}`)
      .then(setItem)
      .catch(err => setError(err.body || err.message))
  }, [id])

  if (error) return <div className="page"><h2>Error</h2><div className="error">{String(error)}</div></div>
  if (!item) return <div className="page"><p>Loading...</p></div>

  return (
    <div className="page">
      <h2>{item.title}</h2>
      <p className="muted">{item.subtitle}</p>
      <p>{item.content}</p>
    </div>
  )
}
