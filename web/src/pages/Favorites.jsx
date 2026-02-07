import { useEffect, useState } from 'react'
import api from '../api'

export default function Favorites() {
  const [recipes, setRecipes] = useState([])
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api('/api/user-recipes')
      .then(setRecipes)
      .catch(err => setError(err.body || err.message))
      .finally(() => setLoading(false))
  }, [])

  async function removeRecipe(id) {
    try {
      await api(`/api/user-recipes/${id}`, { method: 'DELETE' })
      setRecipes(recipes.filter(r => r.id !== id))
    } catch (err) {
      alert('Failed to remove: ' + (err.body || err.message))
    }
  }

  const normalizeImageUrl = (img) => {
    if (!img) return ''
    if (typeof img === 'string' && (img.startsWith('http://') || img.startsWith('https://'))) return img
    return `https://spoonacular.com/recipeImages/${img}`
  }

  return (
    <div className="page">
      <h2>Favorite Recipes</h2>
      <p>Your saved recipes.</p>

      {error && <div className="error">{String(error)}</div>}

      {loading && <p>Loading favorites...</p>}

      {!loading && recipes.length === 0 && !error && (
        <p className="muted">No favorite recipes yet. Save recipes from the Ingredients or Recipes page!</p>
      )}

      <div className="favorites-grid">
        {recipes.map(recipe => (
          <div key={recipe.id} className="card favorite-card">
            {recipe.image && <img src={normalizeImageUrl(recipe.image)} alt={recipe.title} style={{width:'100%', height:'150px', objectFit:'cover', borderRadius: 'var(--radius) var(--radius) 0 0'}} />}
            <div className="card-body">
              <div className="fav-title">{recipe.title}</div>
              {(recipe.usedIngredients !== null || recipe.missedIngredients !== null) && (
                <div className="fav-subtitle">Used: {recipe.usedIngredients || 0} • Missed: {recipe.missedIngredients || 0}</div>
              )}
              <button className="danger sm mt-1" onClick={() => removeRecipe(recipe.id)}>Remove</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
