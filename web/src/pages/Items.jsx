import { Link } from 'react-router-dom'
import { items } from '../mock/data'

export default function Items() {
  return (
    <div className="page container">
      <h2>Items</h2>
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
