# Supabase Table Setup Guide: Equipment (Items) Table

> **🎯 Goal**: Create a database table in Supabase to store equipment data that your Android app can read and display.

---

## ⚡ Quick Start (5-Minute Overview)

**What we're doing in simple terms:**
1. Create a table in Supabase (like an Excel sheet, but in the cloud)
2. Add some sample equipment data
3. Set up security so your app can read the data
4. Connect your Android app to Supabase

**Time needed:** 15-30 minutes (depending on your experience)

**Difficulty:** Beginner-friendly (no coding experience needed for the database part!)

---

## 📦 What You'll Need Before Starting

✅ **A Supabase account** (free account works fine)
- Sign up at: https://supabase.com (it's free!)

✅ **A Supabase project** (we'll create one if you don't have it)

✅ **Your Android Studio project** (the RMS JIMS app)

✅ **Basic computer skills** (copy/paste, clicking buttons)

✅ **About 30 minutes** of your time

**You DON'T need:**
- ❌ SQL knowledge (we'll give you the code to copy)
- ❌ Database experience
- ❌ Advanced programming skills

---

## 📋 Table of Contents

1. [Equipment Attributes Overview](#equipment-attributes-overview) - What data we're storing
2. [Step-by-Step Implementation Guide](#step-by-step-supabase-setup-beginner-friendly-guide) - **START HERE!** 🚀
3. [Testing the Setup](#testing-the-setup-optional-but-recommended) - Verify everything works
4. [Troubleshooting](#troubleshooting-common-issues) - Fix common problems
5. [Table Relationships](#table-relationships) - How tables connect (optional reading)
6. [Database Schema Design](#database-schema-design) - Technical details (optional reading)

---

## 🔍 Equipment Attributes Overview

> **What is this section?** This shows you all the information (attributes/fields) that each equipment item will store. You don't need to memorize this - just know what data we're working with.

**Think of it like a form:** Each equipment item has these fields that need to be filled:

Based on your `Items.kt` data class, here are **ALL** the equipment attributes:

### Core Attributes

| Attribute Name | Type | Required | Description |
|---------------|------|----------|-------------|
| `id` | `INTEGER` | ✅ Yes | Primary key, auto-increment |
| `name` | `TEXT` | ✅ Yes | Equipment name (e.g., "Canon EOS R50 V") |
| `description` | `TEXT` | ✅ Yes | Detailed description of the equipment |
| `image_url` | `TEXT` | ✅ Yes | URL or path to equipment image |
| `is_available` | `BOOLEAN` | ❌ No | Availability status (true/false/null) |
| `created_at` | `TIMESTAMP` | ❌ No | Creation timestamp |

### Foreign Key Relationships

| Attribute Name | Type | Required | References | Description |
|---------------|------|----------|------------|-------------|
| `facility_id` | `INTEGER` | ❌ No | `facilities.id` | Which facility owns this equipment |
| `parent_categoy_id` | `INTEGER` | ❌ No | `item_categories.id` | Parent category (typo in code: "categoy") |
| `category_id` | `INTEGER` | ❌ No | `item_categories.id` or `item_sub_categories.id` | Equipment category |

### JSON/Complex Data

| Attribute Name | Type | Required | Description |
|---------------|------|----------|-------------|
| `specification` | `JSONB` | ❌ No | Flexible JSON object for equipment specs (e.g., resolution, weight, dimensions) |
| `usage_instructions` | `TEXT` | ❌ No | Instructions for using the equipment |

---

## 🗄️ Database Schema Design (Technical Reference)

> **Note**: This section is for reference. You don't need to understand this to complete the setup. The step-by-step guide above has all the code you need to copy and paste.

**What is a schema?** It's the blueprint/structure of your database table - like a template that defines what columns exist and what type of data they store.

### SQL Table Creation Script

```sql
-- Create the 'items' table
CREATE TABLE IF NOT EXISTS items (
    -- Primary Key
    id SERIAL PRIMARY KEY,
    
    -- Core Equipment Information
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT NOT NULL,
    
    -- Availability Status
    is_available BOOLEAN DEFAULT true,
    
    -- Foreign Key Relationships
    facility_id INTEGER REFERENCES facilities(id) ON DELETE SET NULL,
    parent_categoy_id INTEGER REFERENCES item_categories(id) ON DELETE SET NULL,
    category_id INTEGER REFERENCES item_categories(id) ON DELETE SET NULL,
    
    -- JSON Data for Flexible Specifications
    specification JSONB DEFAULT '{}'::jsonb,
    
    -- Additional Information
    usage_instructions TEXT,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create index for faster queries
CREATE INDEX IF NOT EXISTS idx_items_facility_id ON items(facility_id);
CREATE INDEX IF NOT EXISTS idx_items_category_id ON items(category_id);
CREATE INDEX IF NOT EXISTS idx_items_is_available ON items(is_available);
CREATE INDEX IF NOT EXISTS idx_items_name ON items(name);

-- Create trigger to update 'updated_at' timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_updated_at 
    BEFORE UPDATE ON items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add comments for documentation
COMMENT ON TABLE items IS 'Stores equipment/items information for the RMS JIMS system';
COMMENT ON COLUMN items.specification IS 'JSON object containing flexible equipment specifications (e.g., {"resolution": "24MP", "weight": "500g"})';
COMMENT ON COLUMN items.parent_categoy_id IS 'Parent category ID (note: typo in original code preserved)';
```

---

## 📝 Step-by-Step Supabase Setup (Beginner-Friendly Guide)

> **What we're doing**: We're going to create a database table in Supabase that will store all your equipment information. Think of it like creating an Excel spreadsheet, but in the cloud, that your Android app can read from.

---

### 🎯 **PART 1: Getting Started with Supabase**

#### Step 1: Open Supabase Dashboard

**What to do:**
1. Open your web browser (Chrome, Firefox, or Edge)
2. Go to: `https://supabase.com/dashboard`
3. If you're not logged in, click **"Sign In"** and enter your email and password
4. If you don't have an account, click **"Sign Up"** and create a free account

**What you'll see:**
- After logging in, you'll see a list of your projects (or an empty list if this is your first time)

#### Step 2: Select or Create a Project

**If you already have a project:**
- Click on your project name from the list

**If you need to create a new project:**
1. Click the **"New Project"** button (usually green, in the top right)
2. Fill in the details:
   - **Name**: Give it a name like "RMS JIMS" or "My Equipment App"
   - **Database Password**: Create a strong password (save it somewhere safe!)
   - **Region**: Choose the region closest to you
3. Click **"Create new project"**
4. Wait 2-3 minutes for Supabase to set up your project (you'll see a loading screen)

**What you'll see:**
- Once ready, you'll see your project dashboard with a sidebar on the left

---

### 🎯 **PART 2: Finding Your Supabase Credentials**

**Why we need this:** Your Android app needs these credentials to connect to Supabase.

#### Step 3: Get Your Supabase URL and API Key

1. In the left sidebar, click on **"Settings"** (the gear icon at the bottom)
2. Click on **"API"** in the settings menu
3. You'll see two important things:

   **a) Project URL:**
   - Look for **"Project URL"** section
   - Copy the URL (it looks like: `https://xxxxxxxxxxxxx.supabase.co`)
   - This is your `SUPABASE_URL`

   **b) API Keys:**
   - Look for **"Project API keys"** section
   - Find the key labeled **"anon"** or **"public"** (NOT the "service_role" key)
   - Click the eye icon to reveal it, then copy it
   - This is your `SUPABASE_KEY` (it's a long string starting with `eyJ...`)

4. **Save these somewhere safe!** You'll need them later.

**What to do next:**
- Keep this tab open, we'll come back to it
- Or write down the URL and key on a piece of paper

---

### 🎯 **PART 3: Creating the Database Tables**

> **Important**: We need to create tables in a specific order. Think of it like building a house - you need the foundation (categories and facilities) before you can add the walls (items).

#### Step 4: Open the SQL Editor

**What to do:**
1. In the left sidebar, look for **"SQL Editor"** (it has a code icon `</>`)
2. Click on it
3. You'll see a blank editor area
4. Click the **"New query"** button (top left, usually a green "+" button)

**What you'll see:**
- A blank text area where you can type SQL code
- This is where we'll paste our table creation scripts

---

#### Step 5: Create the `item_categories` Table (Foundation Table #1)

**What this table does:** Stores equipment categories like "Cameras", "Microscopes", "Computers", etc.

**What to do:**
1. Copy this entire code block (select all and Ctrl+C / Cmd+C):

```sql
-- Create table for equipment categories
CREATE TABLE IF NOT EXISTS item_categories (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

2. Paste it into the SQL Editor (the blank text area)
3. Click the **"Run"** button (green button at the bottom right)
   - OR press `Ctrl+Enter` (Windows) or `Cmd+Enter` (Mac)

**What you should see:**
- A green checkmark or "Success" message
- The message might say "Success. No rows returned" - this is normal!

**If you see an error:**
- Read the error message carefully
- If it says "table already exists", that's okay! It means the table was created before
- If you see a different error, check that you copied the entire code block

---

#### Step 6: Create the `facilities` Table (Foundation Table #2)

**What this table does:** Stores information about facilities/labs where equipment is located (like "IDC Photo Studio", "Chemistry Lab", etc.)

**What to do:**
1. In the SQL Editor, click **"New query"** again (to start a fresh query)
2. Copy this entire code block:

```sql
-- Create table for facilities/labs
CREATE TABLE IF NOT EXISTS facilities (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    department_id INTEGER,
    type TEXT,
    location TEXT,
    timings TEXT,
    lab_incharge TEXT,
    lab_incharge_phone TEXT,
    lab_incharge_email TEXT,
    prof_incharge TEXT,
    prof_incharge_email TEXT,
    description TEXT,
    branch_id INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

3. Paste it into the SQL Editor
4. Click **"Run"** or press `Ctrl+Enter` / `Cmd+Enter`

**What you should see:**
- "Success. No rows returned" message

---

#### Step 7: Create the Main `items` Table (The Equipment Table)

**What this table does:** This is the main table that stores all your equipment information - names, descriptions, images, availability, etc.

**What to do:**
1. Click **"New query"** again in the SQL Editor
2. Copy this **ENTIRE** code block (it's long, make sure you get all of it):

```sql
-- Create the main 'items' table for equipment
CREATE TABLE IF NOT EXISTS items (
    -- Primary Key (unique ID for each equipment)
    id SERIAL PRIMARY KEY,
    
    -- Core Equipment Information (Required fields)
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT NOT NULL,
    
    -- Availability Status
    is_available BOOLEAN DEFAULT true,
    
    -- Foreign Key Relationships (Links to other tables)
    facility_id INTEGER REFERENCES facilities(id) ON DELETE SET NULL,
    parent_categoy_id INTEGER REFERENCES item_categories(id) ON DELETE SET NULL,
    category_id INTEGER REFERENCES item_categories(id) ON DELETE SET NULL,
    
    -- JSON Data for Flexible Specifications
    specification JSONB DEFAULT '{}'::jsonb,
    
    -- Additional Information
    usage_instructions TEXT,
    
    -- Timestamps (automatically filled)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create indexes for faster searches (this makes queries faster)
CREATE INDEX IF NOT EXISTS idx_items_facility_id ON items(facility_id);
CREATE INDEX IF NOT EXISTS idx_items_category_id ON items(category_id);
CREATE INDEX IF NOT EXISTS idx_items_is_available ON items(is_available);
CREATE INDEX IF NOT EXISTS idx_items_name ON items(name);

-- Create trigger to automatically update 'updated_at' timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_updated_at 
    BEFORE UPDATE ON items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
```

3. Paste it into the SQL Editor
4. Click **"Run"** or press `Ctrl+Enter` / `Cmd+Enter`

**What you should see:**
- "Success. No rows returned" message
- This might take a few seconds because it's creating multiple things

**If you see an error about "facilities" or "item_categories" not existing:**
- Go back to Steps 5 and 6 and make sure those tables were created successfully
- Check the Table Editor (see Step 8) to verify those tables exist

---

#### Step 8: Verify That Tables Were Created

**What to do:**
1. In the left sidebar, click on **"Table Editor"** (it has a table/grid icon)
2. You should see a list of tables including:
   - `item_categories`
   - `facilities`
   - `items`

3. Click on the **`items`** table to see its structure
4. You should see all the columns we created:
   - `id`
   - `name`
   - `description`
   - `image_url`
   - `is_available`
   - `facility_id`
   - `parent_categoy_id`
   - `category_id`
   - `specification`
   - `usage_instructions`
   - `created_at`
   - `updated_at`

**If you don't see the tables:**
- Go back and re-run the SQL scripts
- Make sure you clicked "Run" after pasting each script
- Check for any error messages

---

### 🎯 **PART 4: Adding Sample Data (Testing)**

> **Why we do this:** Before connecting your app, let's add some test equipment so we can verify everything works.

#### Step 9: Add Sample Categories

**What to do:**
1. Go back to **"SQL Editor"**
2. Click **"New query"**
3. Copy and paste this:

```sql
-- Add sample categories
INSERT INTO item_categories (name) 
VALUES ('Cameras'), ('Microscopes'), ('Computers'), ('Lab Equipment')
ON CONFLICT (name) DO NOTHING;
```

4. Click **"Run"**

**What this does:** Creates 4 sample categories that equipment can belong to.

**Verify it worked:**
- Go to **"Table Editor"** → Click `item_categories` table
- You should see 4 rows with names: Cameras, Microscopes, Computers, Lab Equipment

---

#### Step 10: Add Sample Facilities

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- Add sample facilities
INSERT INTO facilities (
    name, 
    department_id, 
    type, 
    location, 
    timings, 
    lab_incharge, 
    lab_incharge_phone, 
    prof_incharge, 
    prof_incharge_email, 
    branch_id
)
VALUES 
(
    'IDC Photo Studio',
    1,
    'Lab',
    'Building A, Floor 2',
    '9 AM - 5 PM',
    'John Doe',
    '+1234567890',
    'Prof. Jane Smith',
    'jane@example.com',
    1
),
(
    'Chemistry Laboratory',
    2,
    'Lab',
    'Building B, Floor 1',
    '8 AM - 6 PM',
    'Alice Johnson',
    '+1234567891',
    'Prof. Bob Williams',
    'bob@example.com',
    1
)
ON CONFLICT DO NOTHING;
```

3. Click **"Run"**

**What this does:** Creates 2 sample facilities where equipment can be located.

**Verify it worked:**
- Go to **"Table Editor"** → Click `facilities` table
- You should see 2 facilities

---

#### Step 11: Add Sample Equipment Items

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- Add sample equipment items
INSERT INTO items (
    name, 
    description, 
    image_url, 
    facility_id, 
    category_id, 
    is_available,
    specification,
    usage_instructions
) VALUES 
(
    'Canon EOS R50 V',
    'Professional camera for photography studio with advanced features',
    'https://images.unsplash.com/photo-1606983340126-99ab4feaa64a?w=400',
    1,  -- This links to the first facility (IDC Photo Studio)
    1,  -- This links to the first category (Cameras)
    true,  -- Available
    '{"resolution": "24.2MP", "sensor": "APS-C", "weight": "375g", "battery": "LP-E17"}'::jsonb,
    'Handle with care. Check battery before use. Use provided lens cap.'
),
(
    'Sony Alpha A7III',
    'High-resolution mirrorless camera with full-frame sensor',
    'https://images.unsplash.com/photo-1606983340126-99ab4feaa64a?w=400',
    1,  -- IDC Photo Studio
    1,  -- Cameras category
    true,  -- Available
    '{"resolution": "24.2MP", "sensor": "Full-frame", "weight": "650g", "battery": "NP-FZ100"}'::jsonb,
    'Check battery before use. Ensure memory card is inserted.'
),
(
    'Nikon D850',
    'DSLR camera with advanced features and high resolution',
    'https://images.unsplash.com/photo-1606983340126-99ab4feaa64a?w=400',
    1,  -- IDC Photo Studio
    1,  -- Cameras category
    false,  -- Not available
    '{"resolution": "45.7MP", "sensor": "Full-frame", "weight": "1005g", "battery": "EN-EL15a"}'::jsonb,
    'Use provided lens cap. Review manual before operation.'
);
```

3. Click **"Run"**

**What this does:** Adds 3 sample cameras to your database.

**Verify it worked:**
- Go to **"Table Editor"** → Click `items` table
- You should see 3 rows with camera equipment
- Scroll right to see all the columns

---

### 🎯 **PART 5: Setting Up Security (Row Level Security)**

> **Why we do this:** By default, Supabase blocks all access for security. We need to allow your app to read the equipment data.

#### Step 12: Enable Row Level Security (RLS)

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- Enable Row Level Security on items table
ALTER TABLE items ENABLE ROW LEVEL SECURITY;
```

3. Click **"Run"**

**What this does:** Turns on security features for the items table.

---

#### Step 13: Create a Policy to Allow Public Read Access

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- Allow anyone (including your app) to read items
CREATE POLICY "Allow public read access to items"
ON items
FOR SELECT
TO public
USING (true);
```

3. Click **"Run"**

**What this does:** Allows your Android app (and anyone) to read/view equipment data. This is safe because we're only allowing reading, not modifying.

**Verify it worked:**
- Go to **"Table Editor"** → Click `items` table
- You should still be able to see the data (if you can't, the policy didn't work)

---

### 🎯 **PART 6: Connecting Your Android App**

#### Step 14: Update Your Android Project's local.properties File

**What to do:**
1. Open Android Studio
2. In the project view (left side), find the file called `local.properties`
   - It's in the root folder of your project (same level as `app` folder)
3. Double-click to open it
4. Add these two lines at the bottom (replace with YOUR actual values from Step 3):

```
SUPABASE_URL=https://your-project-id.supabase.co
SUPABASE_KEY=your-anon-key-here
```

**Example (don't use this, use YOUR values):**
```
SUPABASE_URL=https://abcdefghijklmnop.supabase.co
SUPABASE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFiY2RlZmdoaWprbG1ub3AiLCJyb2xlIjoiYW5vbiIsImlhdCI6MTYzODk2NzIwMCwiZXhwIjoxOTU0NTQzMjAwfQ.example
```

**Important:**
- Replace `your-project-id.supabase.co` with the actual URL you copied in Step 3
- Replace `your-anon-key-here` with the actual anon key you copied in Step 3
- Make sure there are NO spaces around the `=` sign
- Make sure there are NO quotes around the values

5. Save the file (Ctrl+S / Cmd+S)

---

#### Step 15: Rebuild Your Android Project

**Why we do this:** Android needs to rebuild the project to read the new values from `local.properties`.

**What to do:**
1. In Android Studio, go to the top menu: **Build** → **Clean Project**
   - Wait for it to finish (you'll see "Build finished" at the bottom)
2. Then go to: **Build** → **Rebuild Project**
   - Wait for it to finish (this takes longer, maybe 1-2 minutes)

**What you'll see:**
- Progress bar at the bottom showing "Gradle build running..."
- When done, you'll see "Build: finished" or similar

---

#### Step 16: Test the Connection

**What to do:**
1. Connect your Android device or start an emulator
2. Run your app (click the green play button or press Shift+F10)
3. Navigate to the Equipment Screen in your app
4. Open **Logcat** (bottom panel in Android Studio)
5. Look for these log messages:
   - `ItemsApi: Making Supabase query to 'items' table...`
   - `ItemsApi: Supabase query successful, returned X items`

**What you should see:**
- Your app should display the 3 sample cameras we added
- The equipment cards should show: Canon EOS R50 V, Sony Alpha A7III, and Nikon D850

**If you see errors:**
- Check the Troubleshooting section at the bottom of this document
- Make sure you copied the correct URL and key in Step 14
- Make sure you rebuilt the project in Step 15

---

### ✅ **Congratulations!**

If you see your equipment data in the app, you've successfully:
- ✅ Created the database tables
- ✅ Added sample data
- ✅ Set up security
- ✅ Connected your Android app to Supabase

You can now add more equipment through the Supabase dashboard or through your app!

---

## 🔗 Table Relationships

### Entity Relationship Diagram (Conceptual)

```
┌─────────────────┐
│ item_categories │
│  - id (PK)      │
│  - name         │
└────────┬────────┘
         │
         │ (1:N)
         │
┌────────▼────────┐         ┌──────────────┐
│     items       │─────────│  facilities  │
│  - id (PK)      │         │  - id (PK)   │
│  - name         │         │  - name      │
│  - category_id  │─────────└──────────────┘
│  - facility_id  │
│  - ...          │
└─────────────────┘
         │
         │ (1:N)
         │
┌────────▼────────┐
│  item_images    │
│  - id (PK)      │
│  - item_id (FK) │
│  - image_url    │
└─────────────────┘
```

### Foreign Key Constraints

- **`facility_id`** → `facilities.id`
  - When a facility is deleted, set `facility_id` to NULL
  - Allows equipment to exist without a facility

- **`category_id`** → `item_categories.id`
  - When a category is deleted, set `category_id` to NULL
  - Allows equipment to exist without a category

- **`parent_categoy_id`** → `item_categories.id`
  - References parent category (if using hierarchical categories)
  - Can be NULL for top-level items

---

## 📊 Sample Data Insertion

### Insert Sample Equipment Items

```sql
-- First, ensure you have some categories and facilities
-- (Adjust IDs based on your actual data)

-- Sample Category (if needed)
INSERT INTO item_categories (name) 
VALUES ('Cameras') 
ON CONFLICT (name) DO NOTHING;

-- Sample Facility (if needed)
INSERT INTO facilities (name, department_id, type, location, timings, lab_incharge, lab_incharge_phone, prof_incharge, prof_incharge_email, branch_id)
VALUES ('IDC Photo Studio', 1, 'Lab', 'Building A, Floor 2', '9 AM - 5 PM', 'John Doe', '+1234567890', 'Prof. Jane Smith', 'jane@example.com', 1)
ON CONFLICT DO NOTHING;

-- Insert Sample Equipment Items
INSERT INTO items (
    name, 
    description, 
    image_url, 
    facility_id, 
    category_id, 
    is_available,
    specification,
    usage_instructions
) VALUES 
(
    'Canon EOS R50 V',
    'Professional camera for photography studio with advanced features',
    'https://example.com/images/canon-eos-r50.jpg',
    1,  -- facility_id (adjust based on your facilities)
    1,  -- category_id (adjust based on your categories)
    true,
    '{"resolution": "24.2MP", "sensor": "APS-C", "weight": "375g", "battery": "LP-E17"}'::jsonb,
    'Handle with care. Check battery before use. Use provided lens cap.'
),
(
    'Sony Alpha A7III',
    'High-resolution mirrorless camera with full-frame sensor',
    'https://example.com/images/sony-alpha-a7iii.jpg',
    1,
    1,
    true,
    '{"resolution": "24.2MP", "sensor": "Full-frame", "weight": "650g", "battery": "NP-FZ100"}'::jsonb,
    'Check battery before use. Ensure memory card is inserted.'
),
(
    'Nikon D850',
    'DSLR camera with advanced features and high resolution',
    'https://example.com/images/nikon-d850.jpg',
    1,
    1,
    false,
    '{"resolution": "45.7MP", "sensor": "Full-frame", "weight": "1005g", "battery": "EN-EL15a"}'::jsonb,
    'Use provided lens cap. Review manual before operation.'
);

-- Verify the insertions
SELECT id, name, is_available, facility_id, category_id FROM items;
```

### Insert Sample Data with JSON Specifications

The `specification` field accepts any JSON structure. Examples:

```sql
-- Camera with detailed specs
UPDATE items 
SET specification = '{
    "resolution": "24.2MP",
    "sensor_size": "APS-C",
    "iso_range": "100-25600",
    "video": "4K",
    "weight": "375g",
    "dimensions": {"width": "116mm", "height": "86mm", "depth": "69mm"},
    "battery": "LP-E17",
    "connectivity": ["WiFi", "Bluetooth", "USB-C"]
}'::jsonb
WHERE name = 'Canon EOS R50 V';

-- Equipment with different spec structure
INSERT INTO items (name, description, image_url, specification)
VALUES (
    'Microscope XYZ-2000',
    'High-precision laboratory microscope',
    'https://example.com/images/microscope.jpg',
    '{
        "magnification": "1000x",
        "light_source": "LED",
        "stage_type": "Mechanical",
        "eyepiece": "10x",
        "objectives": ["4x", "10x", "40x", "100x"]
    }'::jsonb
);
```

---

## 🔒 Row Level Security (RLS) Policies

### Enable RLS on the Table

```sql
-- Enable Row Level Security
ALTER TABLE items ENABLE ROW LEVEL SECURITY;
```

### Create Policies

#### Policy 1: Allow Public Read Access (for unauthenticated users)

```sql
-- Allow anyone to read items (SELECT)
CREATE POLICY "Allow public read access to items"
ON items
FOR SELECT
TO public
USING (true);
```

#### Policy 2: Allow Authenticated Users Full Access

```sql
-- Allow authenticated users to read, insert, update, delete
CREATE POLICY "Allow authenticated users full access to items"
ON items
FOR ALL
TO authenticated
USING (true)
WITH CHECK (true);
```

#### Policy 3: Allow Service Role Full Access (for admin operations)

```sql
-- Allow service role (backend) full access
CREATE POLICY "Allow service role full access to items"
ON items
FOR ALL
TO service_role
USING (true)
WITH CHECK (true);
```

### Alternative: Role-Based Access Control

If you want more granular control:

```sql
-- Only allow users with 'admin' role to modify
CREATE POLICY "Allow admins to modify items"
ON items
FOR INSERT, UPDATE, DELETE
TO authenticated
USING (
    EXISTS (
        SELECT 1 FROM user_roles 
        WHERE user_id = auth.uid() 
        AND role = 'admin'
    )
);

-- Allow staff to read and update availability
CREATE POLICY "Allow staff to update availability"
ON items
FOR UPDATE
TO authenticated
USING (
    EXISTS (
        SELECT 1 FROM user_roles 
        WHERE user_id = auth.uid() 
        AND role IN ('admin', 'staff')
    )
)
WITH CHECK (
    -- Only allow updating is_available field
    OLD.name = NEW.name AND
    OLD.description = NEW.description
);
```

---

## 🧪 Testing the Setup (Optional but Recommended)

> **Why test?** Testing helps you make sure everything is set up correctly before using it in your app.

### Test 1: Verify Table Structure (Check What We Created)

**What this does:** Shows you all the columns in your `items` table to make sure everything was created correctly.

**What to do:**
1. Go to **SQL Editor** in Supabase
2. Click **"New query"**
3. Copy and paste this:

```sql
-- Check what columns exist in the items table
SELECT 
    column_name, 
    data_type, 
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'items'
ORDER BY ordinal_position;
```

4. Click **"Run"**

**What you should see:**
- A table showing all 12 columns:
  - `id`, `name`, `description`, `image_url`, `is_available`, `facility_id`, `parent_categoy_id`, `category_id`, `specification`, `usage_instructions`, `created_at`, `updated_at`

**If you see fewer columns:**
- Go back to Step 7 and re-run the table creation script

---

### Test 2: Check If Data Was Inserted Correctly

**What this does:** Shows you all the equipment items you added.

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- See all equipment items
SELECT id, name, is_available, facility_id, category_id 
FROM items;
```

3. Click **"Run"**

**What you should see:**
- A table with 3 rows (the cameras we added):
  - Canon EOS R50 V (available)
  - Sony Alpha A7III (available)
  - Nikon D850 (not available)

**If you see 0 rows:**
- Go back to Step 11 and re-run the sample data insertion script

---

### Test 3: Test Querying Available Items Only

**What this does:** Tests if you can filter equipment by availability.

**What to do:**
1. In SQL Editor, click **"New query"**
2. Copy and paste this:

```sql
-- Get only available equipment
SELECT id, name, is_available 
FROM items 
WHERE is_available = true;
```

3. Click **"Run"**

**What you should see:**
- Only 2 rows (Canon and Sony, not Nikon because it's not available)

---

### Test 4: Test from Your Android App (The Real Test!)

**This is the most important test** - it verifies your app can actually connect to Supabase.

**What to do:**
1. **Make sure you completed Steps 14-15** (updated local.properties and rebuilt project)

2. **Run your app** on a device or emulator

3. **Navigate to the Equipment Screen** in your app

4. **Open Logcat in Android Studio:**
   - Look at the bottom panel in Android Studio
   - Click on the **"Logcat"** tab
   - If you don't see it, go to: **View** → **Tool Windows** → **Logcat**

5. **Filter the logs:**
   - In the Logcat search box, type: `ItemsApi`
   - This will show only logs related to fetching equipment

6. **Look for these messages:**
   - ✅ `ItemsApi: Making Supabase query to 'items' table...`
   - ✅ `ItemsApi: Supabase query successful, returned 3 items`
   - ✅ `ItemsViewModel: Received 3 items from repository`

**What you should see in your app:**
- The Equipment Screen should display 3 equipment cards:
  - Canon EOS R50 V
  - Sony Alpha A7III
  - Nikon D850

**If you see errors in Logcat:**
- See the Troubleshooting section below
- Common errors are explained there

---

## 🎯 Summary Checklist

- [ ] Created `item_categories` table (if needed)
- [ ] Created `facilities` table (if needed)
- [ ] Created `items` table with all columns
- [ ] Created indexes for performance
- [ ] Created `updated_at` trigger
- [ ] Inserted sample data
- [ ] Enabled Row Level Security (RLS)
- [ ] Created RLS policies for public read access
- [ ] Tested queries from SQL Editor
- [ ] Updated `local.properties` with Supabase credentials
- [ ] Rebuilt Android project
- [ ] Tested app connection to Supabase
- [ ] Verified equipment data displays in app

---

## 📚 Additional Resources

- [Supabase Documentation](https://supabase.com/docs)
- [PostgreSQL JSONB Documentation](https://www.postgresql.org/docs/current/datatype-json.html)
- [Supabase Row Level Security Guide](https://supabase.com/docs/guides/auth/row-level-security)
- [Supabase API Reference](https://supabase.com/docs/reference)

---

## ⚠️ Important Notes

1. **Column Name Typo**: The code uses `parent_categoy_id` (missing 't' in "category"). The SQL schema preserves this to match your code. If you want to fix it, you'll need to update both the database and the Kotlin code.

2. **JSON Specification Field**: The `specification` field is flexible JSONB. You can store any structure here, but consider documenting the expected format for consistency.

3. **Image URLs**: Currently stored as TEXT. Consider using Supabase Storage for images and storing only the path/URL.

4. **Timestamps**: The `created_at` field is automatically set. Consider adding `updated_at` tracking (already included in the schema).

5. **Foreign Keys**: All foreign keys are nullable (`ON DELETE SET NULL`), meaning equipment can exist without categories or facilities. Adjust based on your business logic.

---

## 🆘 Troubleshooting Common Issues

> **Don't panic!** Most issues have simple solutions. Read through these common problems and their fixes.

---

### ❌ Issue 1: "Table does not exist" Error

**What you see:**
- Error message in SQL Editor: `relation "items" does not exist`
- Or: `ERROR: table "items" does not exist`

**Why this happens:**
- You haven't created the table yet, or the script didn't run successfully

**How to fix:**
1. Go to **SQL Editor** in Supabase
2. Go back to **Step 7** in this guide
3. Copy the entire `CREATE TABLE` script again
4. Paste it and click **"Run"**
5. Make sure you see "Success" message

**How to verify it worked:**
- Go to **Table Editor** → You should see `items` in the list

---

### ❌ Issue 2: "Foreign key constraint violation" Error

**What you see:**
- Error: `insert or update on table "items" violates foreign key constraint`
- Or: `Key (facility_id)=(1) is not present in table "facilities"`

**Why this happens:**
- You're trying to add equipment that references a facility or category that doesn't exist
- For example: You set `facility_id = 1` but there's no facility with `id = 1`

**How to fix:**
1. **Check what facilities exist:**
   - Go to **Table Editor** → Click `facilities` table
   - Note the `id` numbers (they might be 1, 2, 3, etc.)

2. **Check what categories exist:**
   - Go to **Table Editor** → Click `item_categories` table
   - Note the `id` numbers

3. **When inserting items, use the correct IDs:**
   - Use `facility_id` values that actually exist in the `facilities` table
   - Use `category_id` values that actually exist in the `item_categories` table

**Example:**
- If your first facility has `id = 1`, use `facility_id = 1`
- If your first category has `id = 1`, use `category_id = 1`

---

### ❌ Issue 3: "Permission denied" or "401 Unauthorized" in App

**What you see:**
- In Logcat: `ItemsApi: Exception message: 401 Unauthorized`
- Or: `Permission denied for table items`
- App shows empty list or error screen

**Why this happens:**
- Row Level Security (RLS) is blocking access
- You haven't created the policy to allow public read access

**How to fix:**
1. Go to **SQL Editor** in Supabase
2. Go back to **Step 13** in this guide
3. Copy and run the policy creation script again:

```sql
-- Allow anyone to read items
CREATE POLICY "Allow public read access to items"
ON items
FOR SELECT
TO public
USING (true);
```

3. **Verify RLS is enabled:**
   - Go to **Table Editor** → Click `items` table
   - Look for a lock icon or "RLS enabled" indicator
   - If RLS is not enabled, go back to **Step 12** and enable it

---

### ❌ Issue 4: "Invalid JSON" Error

**What you see:**
- Error: `invalid input syntax for type jsonb`
- Or: `syntax error, unexpected token`

**Why this happens:**
- The JSON in the `specification` field has a syntax error
- Missing quotes, commas, or brackets

**How to fix:**
1. **Check your JSON syntax:**
   - JSON must use double quotes `"` not single quotes `'`
   - All keys must be in quotes: `"resolution"` not `resolution`
   - Make sure you have `::jsonb` at the end

2. **Correct format example:**
```sql
-- ✅ CORRECT:
'{"resolution": "24.2MP", "weight": "375g"}'::jsonb

-- ❌ WRONG (missing quotes on keys):
'{resolution: "24.2MP", weight: "375g"}'::jsonb

-- ❌ WRONG (using single quotes):
'{"resolution": '24.2MP', "weight": '375g'}'::jsonb
```

3. **Use an online JSON validator** if you're not sure:
   - Go to https://jsonlint.com
   - Paste your JSON (without the `::jsonb` part)
   - It will tell you if there are errors

---

### ❌ Issue 5: App Shows Empty List (No Equipment)

**What you see:**
- App runs without errors
- Equipment screen is empty (no equipment cards)
- Logcat shows: `ItemsApi: Supabase query successful, returned 0 items`

**Why this happens:**
- No data in the `items` table
- Or the query is working but finding no rows

**How to fix:**

**Step 1: Check if data exists in Supabase**
1. Go to **Table Editor** in Supabase
2. Click on `items` table
3. Do you see any rows? If NO:
   - Go back to **Step 11** and add sample data again

**Step 2: Check your Supabase credentials**
1. Open `local.properties` in Android Studio
2. Verify `SUPABASE_URL` and `SUPABASE_KEY` are correct
3. Make sure there are NO extra spaces or quotes
4. The URL should start with `https://`
5. The key should start with `eyJ`

**Step 3: Rebuild the project**
1. **Build** → **Clean Project**
2. **Build** → **Rebuild Project**
3. Wait for it to finish
4. Run the app again

**Step 4: Check Logcat for errors**
1. Open Logcat in Android Studio
2. Filter by: `ItemsApi` or `ItemsViewModel`
3. Look for any red error messages
4. Read the error and check the solutions above

---

### ❌ Issue 6: "Connection refused" or Network Error

**What you see:**
- Logcat: `Failed to connect` or `Connection refused`
- Or: `Network error` or `Timeout`

**Why this happens:**
- Internet connection issue
- Wrong Supabase URL
- Supabase project is paused (free tier)

**How to fix:**

**Step 1: Check your internet connection**
- Make sure your device/emulator has internet
- Try opening a website in the browser

**Step 2: Verify Supabase URL**
1. Go to Supabase Dashboard → **Settings** → **API**
2. Copy the **Project URL** again
3. Make sure it matches what's in `local.properties`
4. Make sure it starts with `https://` (not `http://`)

**Step 3: Check if Supabase project is active**
1. Go to Supabase Dashboard
2. If you see a message about "Project paused", click to resume it
3. Free tier projects pause after inactivity

**Step 4: Test the connection**
1. In Supabase Dashboard, go to **Table Editor**
2. Click on `items` table
3. If you can see the data here, Supabase is working
4. The issue is with your app connection

---

### ❌ Issue 7: "BuildConfig.SUPABASE_URL is empty" Error

**What you see:**
- App crashes on startup
- Error: `Supabase URL missing` or `BuildConfig.SUPABASE_URL is empty`

**Why this happens:**
- The values in `local.properties` aren't being read
- You didn't rebuild the project after updating `local.properties`

**How to fix:**
1. **Double-check `local.properties`:**
   - Open the file
   - Make sure the lines are exactly:
     ```
     SUPABASE_URL=https://your-project-id.supabase.co
     SUPABASE_KEY=your-anon-key-here
     ```
   - No spaces around `=`
   - No quotes
   - No typos

2. **Clean and rebuild:**
   - **Build** → **Clean Project** (wait for it to finish)
   - **Build** → **Rebuild Project** (wait 1-2 minutes)
   - **File** → **Invalidate Caches** → **Invalidate and Restart** (if still not working)

3. **Verify the file location:**
   - `local.properties` should be in the **root** of your project
   - Same folder as `app`, `build.gradle`, etc.
   - NOT inside the `app` folder

---

### ❌ Issue 8: Can't Find SQL Editor or Table Editor

**What you see:**
- Can't find these options in Supabase dashboard

**How to fix:**
1. **SQL Editor:**
   - Look in the **left sidebar** for an icon that looks like `</>` or code brackets
   - It might be labeled "SQL Editor" or just have a code icon
   - If you still can't find it, try: **Database** → **SQL Editor**

2. **Table Editor:**
   - Look in the **left sidebar** for an icon that looks like a table/grid
   - It might be labeled "Table Editor" or "Tables"
   - If you still can't find it, try: **Database** → **Tables**

3. **If you're on mobile:**
   - Some features are easier on desktop
   - Try using a computer or tablet

---

### 💡 Still Having Issues?

**Try these general steps:**
1. **Read the error message carefully** - it usually tells you what's wrong
2. **Check Logcat** - Android Studio's Logcat shows detailed error messages
3. **Verify each step** - Go back and make sure you completed each step
4. **Start fresh** - If really stuck, you can delete the tables and start over:
   - In SQL Editor, run: `DROP TABLE IF EXISTS items;`
   - Then start from Step 7 again

**Get help:**
- Check Supabase documentation: https://supabase.com/docs
- Check Supabase Discord community
- Review the error message and search for it online

---

---

## 🎉 You're Done! What's Next?

### ✅ Completion Checklist

Use this checklist to make sure you completed everything:

**Database Setup:**
- [ ] Created `item_categories` table
- [ ] Created `facilities` table  
- [ ] Created `items` table
- [ ] Added sample categories (Cameras, Microscopes, etc.)
- [ ] Added sample facilities (IDC Photo Studio, etc.)
- [ ] Added sample equipment items (at least 3 cameras)
- [ ] Enabled Row Level Security (RLS)
- [ ] Created public read policy

**Android App Connection:**
- [ ] Got Supabase URL from Settings → API
- [ ] Got Supabase anon key from Settings → API
- [ ] Updated `local.properties` with both values
- [ ] Cleaned and rebuilt the Android project
- [ ] Ran the app and saw equipment data displayed
- [ ] Checked Logcat and saw successful connection messages

**If all boxes are checked:** 🎊 **Congratulations!** Your setup is complete!

---

### 🚀 Next Steps (After Setup is Complete)

**1. Add Real Equipment Data:**
   - Go to Supabase → Table Editor → `items` table
   - Click "Insert row" to add your actual equipment
   - Or use the SQL Editor to insert multiple items at once

**2. Add More Categories:**
   - Go to Table Editor → `item_categories`
   - Add categories like: "Laptops", "Projectors", "Audio Equipment", etc.

**3. Add More Facilities:**
   - Go to Table Editor → `facilities`
   - Add all your actual labs and facilities

**4. Customize Your Data:**
   - Update equipment images (use real image URLs)
   - Add detailed specifications in the JSON field
   - Add usage instructions for each equipment

**5. Test Different Features:**
   - Try filtering equipment by category
   - Test the search functionality
   - Verify availability status works correctly

---

### 📚 Learning More (Optional)

**Want to understand more about what we did?**

- **What is a database table?** Think of it like an Excel spreadsheet with rows (equipment items) and columns (name, description, etc.)

- **What is Row Level Security (RLS)?** It's like a bouncer at a club - it controls who can read or modify your data. We set it to allow anyone to read (view) but not modify.

- **What is a foreign key?** It's like a reference/link between tables. For example, `facility_id` links an equipment item to a specific facility.

- **What is JSONB?** It's a flexible way to store structured data. The `specification` field uses this so you can store different types of information for different equipment.

---

### 🆘 Need Help?

**Common Questions:**

**Q: Can I add equipment through the app instead of Supabase?**  
A: Yes! But you'll need to implement that feature in your app code. For now, you can add data through Supabase's Table Editor.

**Q: How do I delete equipment?**  
A: Go to Table Editor → `items` table → Click on a row → Click the delete/trash icon

**Q: Can I change the table structure later?**  
A: Yes, but be careful. You can add columns, but removing columns might break your app if it expects those columns to exist.

**Q: How do I backup my data?**  
A: Supabase automatically backs up your data. You can also export it: Settings → Database → Backups

**Q: What if I make a mistake?**  
A: Don't worry! You can always:
   - Delete rows and re-add them
   - Drop tables and recreate them (be careful - this deletes all data!)
   - Start a new project if needed

---

### 📖 Additional Resources

- **Supabase Official Docs**: https://supabase.com/docs
- **Supabase YouTube Tutorials**: Search "Supabase tutorial" on YouTube
- **PostgreSQL Basics**: https://www.postgresql.org/docs/ (if you want to learn SQL)

---

**Last Updated**: November 2024  
**Version**: 2.0 (Beginner-Friendly Edition)

**Made with ❤️ for RMS JIMS Project**
