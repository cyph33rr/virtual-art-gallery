# AURA Gallery - Features & Usage Guide

## ✅ Updates Completed (April 14, 2026)

### 1. Enhanced Checkout Function
**What Changed:**
- Added purchase confirmation dialog showing:
  - Number of items in cart
  - Total price in INR (₹)
  - Option to confirm or cancel
- Better error handling with detailed error messages
- Loading state feedback during checkout
- Improved success messages

**How to Use:**
1. Add artworks to cart by clicking **"+ Cart"** button on artwork cards
2. Click cart icon (🛒) in top-right navigation
3. Review items and total price
4. Click **"Proceed to Checkout"**
5. Confirm purchase in the dialog
6. Wait for order processing

---

## 2. Stock/Inventory Display
**What Changed:**
- Each artwork card now displays available stock
- Shows as: "Stock: X pieces" or "Stock: 1 piece"
- Located below the price and category

**Example:**
- Limited Edition: "Stock: 1 piece"
- Multiple Prints: "Stock: 5 pieces"

**Note:** Backend must return `stock` field for this to display. Default is 1 if not provided.

---

## 3. Currency Changed to Indian Rupees (INR)
**What Changed:**
- All prices now display in ₹ (Rupees) instead of $
- Applies to:
  - Gallery cards
  - Cart items
  - 3D viewer preview
  - Checkout confirmation

**Example:**
- Old: $1200
- New: ₹1200

---

## 4. How to Add Your Own Artworks

### Prerequisites:
- You must be logged in
- Your account role must be **ARTIST**

### Step-by-Step:

#### 1️⃣ Sign Up as an Artist
1. Click **"Join"** button in top-right
2. Fill in your details:
   - Full Name
   - Email
   - Password (minimum 6 characters)
   - **Select "Artist — Upload & Sell"** for role
3. Click **"Create Account"**
4. You'll be logged in automatically

#### 2️⃣ Access Upload Section
1. Look for **"Upload"** link in navigation (only visible for artists)
2. Click it to expand the upload section
3. Scroll down to see the upload form

#### 3️⃣ Upload Your Artwork

**A) Upload Image:**
- Either:
  - Click in the upload zone and select an image file
  - Drag & drop an image into the zone
- Supported formats: JPG, PNG, GIF, WebP
- Maximum recommended size: 5MB

**B) Fill in Details:**
- **Title**: Name of your artwork (e.g., "Sunset Over Mountains")
- **Price (in INR)**: Price in Indian Rupees (e.g., 2500)
  - Note: This is now in ₹, not $
- **Category**: Choose from:
  - Abstract
  - Landscape
  - Portrait
  - Sculpture
  - Digital
- **Medium**: What you used (e.g., "Oil on Canvas", "Digital", "Watercolor")
- **Description**: Tell story about your artwork (optional but recommended)

#### 4️⃣ Publish
1. Click **"Publish Artwork"** button
2. Wait for success message
3. Your artwork will appear in the gallery immediately
4. Scroll down to gallery section to see it

---

## 5. Example Artworks to Upload

### Landscape Example:
```
Title: Golden Hour Valley
Price: 5500
Category: Landscape
Medium: Acrylic on Canvas
Description: A serene valley at sunset with warm golden light casting long shadows across the terrain. This piece captures the tranquility of nature's most magical hour.
```

### Abstract Example:
```
Title: Chromatic Dreams
Price: 3200
Category: Abstract
Medium: Digital
Description: An exploration of color and form without representation. Bold geometric shapes intersect with fluid gradients, creating visual harmony through contrast.
```

### Portrait Example:
```
Title: Soul's Window
Price: 8900
Category: Portrait
Medium: Oil on Canvas
Description: A contemplative portrait exploring the depth of human emotion through careful attention to light and shadow.
```

---

## 6. Viewing & Purchasing Process

### For Collectors:
1. Browse gallery by category or view all
2. Hover over artwork to see overlay buttons
3. Click **"View in 3D"** to explore in immersive viewer with multiple environments:
   - 🏛 Gallery
   - 🎨 Studio
   - 🌿 Outdoor
   - 🏺 Museum
   - 🌙 Night Exhibition
4. Click **"+ Cart"** to add to collection
5. Review in cart sidebar (🛒)
6. Proceed to checkout

### 3D Viewer Controls:
- **Mouse**: Click and drag to rotate artwork
- **Scroll**: Zoom in/out
- **Environment Buttons**: Switch viewing setting
- **Tool Buttons**: 
  - ↻ Rotate (continuous)
  - ⊕ Zoom (manual)
  - ⌖ Reset view

---

## 7. Database Integration

### Backend Expects These Fields for Artworks:
```json
{
  "id": 1,
  "title": "Artwork Title",
  "artistName": "Artist Name",
  "category": "Abstract",
  "price": 2500,
  "medium": "Oil on Canvas",
  "description": "Description...",
  "stock": 1,
  "sold": false,
  "imageUrl": "/uploads/filename.jpg",
  "createdAt": "2026-04-14T12:00:00Z"
}
```

### Optional Backend Enhancement:
Add `stock` field to your artwork model to control inventory:
```sql
ALTER TABLE artworks ADD COLUMN stock INT DEFAULT 1;
```

---

## 8. Troubleshooting

### Upload Not Working?
- ✓ Verify you're logged in as ARTIST
- ✓ Check all required fields are filled
- ✓ Image file format is JPG/PNG/GIF/WebP
- ✓ Open browser console (F12) for detailed error

### Artwork Not Appearing in Gallery?
- ✓ Refresh page (Ctrl+F5)
- ✓ Check if upload was actually successful
- ✓ Verify image file is in `/backend/uploads/` folder
- ✓ Check backend logs for errors

### Checkout Issues?
- ✓ Verify you're logged in
- ✓ Check internet connection
- ✓ Ensure backend is running on port 8080
- ✓ Check browser console for error details

### Images Not Loading?
- ✓ Verify images exist in `/backend/uploads/`
- ✓ Check CORS settings in backend
- ✓ Try hard refresh (Ctrl+Shift+R)

---

## 9. API Endpoints Used

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/signup` | POST | Register as artist or collector |
| `/api/auth/login` | POST | Sign in |
| `/api/artworks` | GET | Fetch all/filtered artworks |
| `/api/artworks?category=Abstract` | GET | Filter by category |
| `/api/artworks` | POST | Upload new artwork (artist only) |
| `/api/orders/checkout` | POST | Process purchase |

---

## 10. Tips for Success

1. **High-Quality Images**: Use clear, well-lit photos of your artwork
2. **Detailed Descriptions**: Help buyers understand your vision
3. **Competitive Pricing**: Research similar artworks in your category
4. **Multiple Categories**: Share diverse work to reach broader audience
5. **Regular Updates**: Keep uploading new pieces to attract collectors

---

## 🎉 You're Ready!

Your AURA Gallery is now fully functional with:
- ✓ Stock management
- ✓ INR pricing
- ✓ Enhanced checkout
- ✓ Artist upload system
- ✓ Immersive 3D viewer

**Start adding your artworks today!**
