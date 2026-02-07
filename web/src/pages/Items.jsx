import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'
import api from '../api'

export default function Items() {
  const [items, setItems] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    api('/api/items')
      .then(setItems)
      .catch(err => setError(err.body || err.message))
  }, [])

  return (
    <div className="page">
      <h2>Items</h2>
      {error && <div className="error">{String(error)}</div>}
      <ul className="item-list">
        {items.map(it => (
          <li key={it.id} className="item">
            <Link to={`/items/${it.id}`}>
              <strong>{it.title}</strong>
              <p className="muted">{it.subtitle}</p>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
