import { useState } from 'react'
import api from '../api'

export default function IngredientSearch({ onSelect }) {
  const [q, setQ] = useState('')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  async function doSearch(e) {
    e && e.preventDefault()
    if (!q) return
    setLoading(true)
    setError(null)
    try {
      const res = await api(`/api/spoon/ingredients?query=${encodeURIComponent(q)}`)
      // Spoonacular returns {results: [...]}
      setResults(res.results || [])
    } catch (err) {
      setError(err.body || err.message)
    } finally {
      setLoading(false)
    }
  }

  async function addIngredient(item) {
    // Save to user_ingredients table
    const payload = {
      spoonacularId: item.id,
      name: item.name,
      aisle: item.aisle || ''
    }
    try {
      await api('/api/user-ingredients', { method: 'POST', body: JSON.stringify(payload) })
      // optionally notify parent
      if (onSelect) onSelect(item)
      alert('Saved "' + item.name + '" to your ingredients')
    } catch (err) {
      if (err.body && err.body.includes('already saved')) {
        alert('Ingredient already saved')
      } else {
        alert('Failed to save: ' + (err.body || err.message))
      }
    }
  }

  return (
    <div className="ingredient-search">
      <form onSubmit={doSearch} style={{display:'flex', gap:'.5rem', marginBottom:'1rem'}}>
        <input value={q} onChange={e=>setQ(e.target.value)} placeholder="Search ingredients (e.g. banana)" style={{flex:1}} />
        <button type="submit">Search</button>
      </form>

      {loading && <p>Searching...</p>}
      {error && <div className="error">{error}</div>}

      <div className="ingredient-grid">
        {results.map(item => (
          <div key={item.id} className="card ingredient-card">
            <div className="ing-name">{item.name}</div>
            <div className="ing-aisle">{item.aisle || ''}</div>
            <button className="sm mt-1" onClick={() => addIngredient(item)}>Add</button>
          </div>
        ))}
      </div>
    </div>
  )
}
