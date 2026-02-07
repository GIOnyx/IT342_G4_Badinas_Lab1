import { useState, useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import api from '../api'

export default function Recipes() {
  const location = useLocation()
  const [query, setQuery] = useState('')
  const [recipes, setRecipes] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [searchType, setSearchType] = useState('name')

  // Load recipes from navigation state (from Ingredients page)
  useEffect(() => {
    if (location.state?.recipes) {
      setRecipes(location.state.recipes)
      setSearchType(location.state.searchType || 'ingredients')
    }
  }, [location.state])

  async function searchRecipes(e) {
    e && e.preventDefault()
    if (!query) return
    setLoading(true)
    setError(null)
    setSearchType('name')
    try {
      // Use Spoonacular's recipe search endpoint
      const res = await api(`/api/spoon/recipes/search?query=${encodeURIComponent(query)}&number=12`)
      // complexSearch returns {results: [...], ...} format
      const results = res.results || res || []
      setRecipes(results)
    } catch (err) {
      const errorMsg = err.body || err.message
      setError(errorMsg)
      // Check if it's a rate limit error
      if (errorMsg.includes('402') || errorMsg.includes('quota') || errorMsg.includes('limit')) {
        alert('Spoonacular API daily limit reached. Please try again tomorrow or use a different API key.')
      } else {
        alert('Error searching recipes: ' + errorMsg)
      }
    } finally {
      setLoading(false)
    }
  }

  async function saveRecipe(recipe) {
    try {
      await api('/api/user-recipes', {
        method: 'POST',
        body: JSON.stringify({
          spoonacularRecipeId: recipe.id,
          title: recipe.title,
          image: recipe.image,
          usedIngredients: recipe.usedIngredientCount || null,
          missedIngredients: recipe.missedIngredientCount || null
        })
      })
      alert('Recipe saved to favorites!')
    } catch (err) {
      if (err.body && err.body.includes('already saved')) {
        alert('Recipe already in favorites')
      } else {
        alert('Failed to save: ' + (err.body || err.message))
      }
    }
  }

  const normalizeImageUrl = (img) => {
    if (!img) return ''
    if (typeof img === 'string' && (img.startsWith('http://') || img.startsWith('https://'))) return img
    return `https://spoonacular.com/recipeImages/${img}`
  }

  return (
    <div className="page-wide">
      <h2>Recipe Search</h2>
      <p>Search for recipes by name, cuisine, or dish type.</p>

      <form onSubmit={searchRecipes} style={{display:'flex', gap:'.5rem', marginBottom:'2rem', maxWidth:'600px'}}>
        <input 
          value={query} 
          onChange={e => setQuery(e.target.value)} 
          placeholder="Search recipes (e.g., pasta, chicken curry, tacos)" 
          style={{flex:1}} 
        />
        <button type="submit">Search</button>
      </form>

      <div className="recipes-section">
        {loading && <p>Searching recipes...</p>}
        {error && <div className="error">{error}</div>}
        
        {recipes.length > 0 && (
          <>
            <h3>{searchType === 'ingredients' ? 'Recipe Suggestions' : 'Recipe Results'}</h3>
            {searchType === 'ingredients' && (
              <p className="muted" style={{marginBottom: '1rem'}}>Based on your selected ingredients</p>
            )}
            <div className="recipe-grid">
              {recipes.map(r => (
                <div key={r.id} className="card recipe-card">
                  {r.image && <img src={normalizeImageUrl(r.image)} alt={r.title} />}
                  <div className="card-body">
                    <div className="recipe-title">{r.title}</div>
                    {(r.usedIngredientCount !== undefined || r.missedIngredientCount !== undefined) && (
                      <div className="recipe-meta">
                        Used: {r.usedIngredientCount || 0} &bull; Missed: {r.missedIngredientCount || 0}
                      </div>
                    )}
                    <button className="sm mt-1" onClick={() => saveRecipe(r)}>Save to Favorites</button>
                  </div>
                </div>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  )
}
