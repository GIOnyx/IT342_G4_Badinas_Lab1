import { useParams } from 'react-router-dom'
import { items } from '../mock/data'

export default function ItemDetails() {
  const { id } = useParams()
  const it = items.find(i => String(i.id) === String(id))

  if (!it) return <div className="page container"><h2>Not found</h2></div>

  return (
    <div className="page container">
      <h2>{it.title}</h2>
      <p className="muted">{it.subtitle}</p>
      <p>{it.content}</p>
    </div>
  )
}
