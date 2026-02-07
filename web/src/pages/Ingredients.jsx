import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

// Category-specific search queries to get relevant ingredients
const CATEGORY_QUERIES = {
  dairy: ['milk', 'cheese', 'yogurt', 'butter', 'cream'],
  poultry: ['chicken', 'turkey', 'duck', 'goose'],
  meats: ['beef', 'pork', 'lamb', 'bacon', 'ham'],
  seafood: ['salmon', 'tuna', 'cod', 'fish'],
  shellfish: ['shrimp', 'crab', 'lobster', 'clam', 'oyster'],
  vegetables: ['tomato', 'carrot', 'onion', 'pepper', 'lettuce'],
  fruits: ['apple', 'banana', 'orange', 'strawberry', 'grape'],
  spices: ['salt', 'pepper', 'garlic', 'basil', 'oregano'],
  grains: ['rice', 'wheat', 'oat', 'corn', 'quinoa']
}

const CATEGORIES = [
  'dairy', 'poultry', 'meats', 'seafood', 'shellfish', 'vegetables', 'fruits', 'spices', 'grains'
]

// Aisle keywords we expect to see in Spoonacular meta/aisle fields for each category.
// If an ingredient returns an explicit aisle that does NOT match its category, we skip it.
const CATEGORY_AISLE_KEYWORDS = {
  dairy: ['dairy', 'milk', 'cheese', 'butter', 'eggs'],
  poultry: ['poultry', 'poultry products', 'chicken', 'turkey', 'duck'],
  meats: ['meat', 'beef', 'pork', 'lamb'],
  seafood: ['seafood', 'fish', 'seafood products', 'seafood counter'],
  shellfish: ['shellfish', 'shrimp', 'crab', 'oyster', 'clam'],
  vegetables: ['produce', 'vegetables', 'produce and refrigerated'],
  fruits: ['produce', 'fruits', 'produce and refrigerated'],
  spices: ['spices', 'seasonings', 'condiments'],
  grains: ['pasta', 'rice', 'grains', 'cereal']
}

export default function Ingredients() {
  const navigate = useNavigate()
  const [active, setActive] = useState(CATEGORIES[0])
  const [lists, setLists] = useState({})
  const [loading, setLoading] = useState(false)
  const [selected, setSelected] = useState([])
  const [error, setError] = useState(null)
  const [searchQuery, setSearchQuery] = useState('')

  useEffect(() => {
    fetchCategory(active)
    loadSavedIngredients()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  async function loadSavedIngredients() {
    try {
      const saved = await api('/api/user-ingredients')
      if (Array.isArray(saved)) {
        // Convert saved ingredients to the same format as autocomplete results
        const converted = saved.map(s => ({
          id: s.spoonacularId,
          name: s.name,
          aisle: s.aisle
        }))
        setSelected(converted)
      }
    } catch (err) {
      console.error('Failed to load saved ingredients:', err)
    }
  }

  async function fetchCategory(cat) {
    if (lists[cat]) { setActive(cat); return }
    setLoading(true)
    setError(null)
    try {
      // Fetch ingredients using multiple relevant queries for better coverage
      const queries = CATEGORY_QUERIES[cat] || [cat]
      const allResults = []
      const seenIds = new Set()

      for (const query of queries) {
        try {
          const res = await api(`/api/spoon/ingredients/autocomplete?query=${encodeURIComponent(query)}&number=10&metaInformation=true`)
          // Filter out duplicates and add to results
          if (Array.isArray(res)) {
            res.forEach(item => {
              if (!item || !item.id) return
              if (seenIds.has(item.id)) return

              // If API returned aisle/meta information, ensure it matches the category.
              // Only filter out items that are clearly in the WRONG category
              const aisleRaw = (item.aisle || item.meta?.aisle || item.metaInformation?.aisle || '')
              const aisle = String(aisleRaw || '').toLowerCase()
              
              // Define wrong categories to exclude (more lenient - only block obvious mismatches)
              const wrongCategories = {
                dairy: ['meat', 'seafood', 'produce'],
                poultry: ['dairy', 'seafood', 'produce', 'baking'],
                meats: ['dairy', 'seafood', 'produce'],
                seafood: ['meat', 'dairy', 'produce'],
                shellfish: ['meat', 'dairy', 'produce'],
                vegetables: ['meat', 'dairy', 'seafood'],
                fruits: ['meat', 'dairy', 'seafood'],
                spices: ['meat', 'dairy', 'produce'],
                grains: ['meat', 'dairy', 'seafood']
              }
              
              const blocked = wrongCategories[cat] || []
              if (aisle && blocked.length > 0) {
                const isWrongCategory = blocked.some(bad => aisle.includes(bad))
                if (isWrongCategory) return
              }

              seenIds.add(item.id)
              allResults.push(item)
            })
          }
        } catch (err) {
          console.warn(`Failed to fetch ingredients for query: ${query}`, err)
        }
      }

      setLists(prev => ({ ...prev, [cat]: allResults }))
      setActive(cat)
    } catch (err) {
      setError(err.body || err.message)
    } finally {
      setLoading(false)
    }
  }

  async function toggle(item) {
    const exists = selected.find(s => s.id === item.id)
    if (exists) {
      // Remove from database using Spoonacular ID endpoint
      try {
        await api(`/api/user-ingredients/spoonacular/${item.id}`, { method: 'DELETE' })
        setSelected(selected.filter(s => s.id !== item.id))
      } catch (err) {
        alert('Failed to remove ingredient: ' + (err.body || err.message))
      }
    } else {
      // Add to database
      try {
        await api('/api/user-ingredients', {
          method: 'POST',
          body: JSON.stringify({
            spoonacularId: item.id,
            name: item.name,
            aisle: item.aisle || item.meta?.aisle || item.metaInformation?.aisle || ''
          })
        })
        setSelected([...selected, item])
      } catch (err) {
        if (err.body && err.body.includes('already saved')) {
          alert('Ingredient already added')
        } else {
          alert('Failed to add ingredient: ' + (err.body || err.message))
        }
      }
    }
  }

  function removeChip(id) {
    // Find the ingredient to remove
    const ingredient = selected.find(s => s.id === id)
    if (ingredient) {
      toggle(ingredient)
    }
  }

  async function findRecipes() {
    if (selected.length === 0) return
    try {
      const names = selected.map(s => s.name).join(',')
      const res = await api(`/api/spoon/recipes-by-ingredients?ingredients=${encodeURIComponent(names)}&number=12`)
      // Navigate to recipes page with results
      navigate('/recipes', { state: { recipes: res || [], searchType: 'ingredients' } })
    } catch (err) {
      const errorMsg = err.body || err.message
      if (errorMsg.includes('402') || errorMsg.includes('quota') || errorMsg.includes('limit')) {
        alert('Spoonacular API daily limit reached. Please try again tomorrow or use a different API key.')
      } else {
        alert('Error finding recipes: ' + errorMsg)
      }
    }
  }



  const filteredIngredients = (lists[active] || []).filter(it => 
    it.name.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="ingredients-page">
      <div className="ingredients-header">
        <div>
          <h2>Ingredient Selection</h2>
          <p className="muted">Choose ingredients to find matching recipes</p>
        </div>
        <div className="ingredients-actions">
          <button 
            onClick={findRecipes} 
            disabled={selected.length === 0}
            className="lg"
          >
            Find Recipes ({selected.length})
          </button>
        </div>
      </div>

      {/* Selected items section */}
      {selected.length > 0 && (
        <div className="selected-section">
          <div className="selected-header">
            <h4>Selected Ingredients</h4>
            <span className="selected-count">{selected.length}</span>
          </div>
          <div className="selected-chips">
            {selected.map(s => (
              <span key={s.id} className="chip">
                {s.name}
                <button className="chip-x" onClick={() => removeChip(s.id)}>&times;</button>
              </span>
            ))}
          </div>
        </div>
      )}

      <div className="ingredients-layout">
        {/* Category sidebar */}
        <div className="cat-sidebar">
          <h4>CATEGORIES</h4>
          {CATEGORIES.map(cat => (
            <button
              key={cat}
              className={`secondary ${cat === active ? 'active' : ''}`}
              onClick={() => { fetchCategory(cat); setSearchQuery('') }}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Main area */}
        <div className="ingredients-main">
          {/* Search bar */}
          <div className="ingredients-search">
            <input 
              type="text"
              placeholder={`Search in ${active}...`}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          {error && <div className="error">{error}</div>}
          {loading && <p>Loading ingredients...</p>}

          {/* Ingredient grid */}
          {!loading && filteredIngredients.length === 0 ? (
            <div className="empty-state">
              <p>No ingredients found</p>
            </div>
          ) : (
            <div className="ingredient-grid">
              {filteredIngredients.map(it => (
                <div
                  key={it.id}
                  className={`ingredient-card ${selected.find(s => s.id === it.id) ? 'selected' : ''}`}
                  onClick={() => toggle(it)}
                >
                  <div className="ing-check">✓</div>
                      <div className="ing-name">{it.name}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
