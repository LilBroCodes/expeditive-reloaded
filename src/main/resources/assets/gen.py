import os
import json
import re

# === CONFIG ===
namespace = "expeditive_reloaded"
base_model_path = f"{namespace}/models/item"
texture_path = f"{namespace}:item/bamboo_flute/bamboo_flute_"
playing_texture_path = f"{namespace}:item/bamboo_flute/bamboo_flute_"
colors = [
    "white", "orange", "magenta", "light_blue",
    "yellow", "lime", "pink", "gray",
    "light_gray", "cyan", "purple", "blue",
    "brown", "green", "red", "black"
]

# === TRANSFORM SETTINGS ===
# Base transform used for normal model display
base_display = {
    "thirdperson_righthand": {
        "rotation": [0, 180, 0],
        "translation": [-1.5, 2.7, 1],
        "scale": [0.55, 0.55, 0.55]
    },
    "thirdperson_lefthand": {
        "rotation": [0, 0, 0],
        "translation": [-1.5, 2.7, 1],
        "scale": [0.55, 0.55, 0.55]
    },
    "firstperson_righthand": {
        "rotation": [0, -90, 25],
        "translation": [1.13, 1, 1.13],
        "scale": [0.68, 0.68, 0.68]
    },
    "firstperson_lefthand": {
        "rotation": [0, 90, -25],
        "translation": [1.13, 1, 1.13],
        "scale": [0.68, 0.68, 0.68]
    }
}

# Playing transform variant (more dramatic)
playing_display = {
    "thirdperson_righthand": {
        "rotation": [0, -30, 30],
        "translation": [-3, 1, 5],
        "scale": [0.5, 0.5, 0.5]
    },
    "thirdperson_lefthand": {
        "rotation": [0, -30, -30],
        "translation": [-2, 1, 5],
        "scale": [0.5, 0.5, 0.5]
    },
    "firstperson_righthand": {
        "rotation": [5, 0, 40],
        "translation": [0.2, -3, -3],
        "scale": [0.3, 0.3, 0.3]
    },
    "firstperson_lefthand": {
        "rotation": [5, 0, -40],
        "translation": [0.2, -3, -3],
        "scale": [0.3, 0.3, 0.3]
    }
}

def write_json(path, data):
    raw = json.dumps(data, indent=4)
    # Compact short arrays like [0, 180, 0] into one line
    compact = re.sub(r'\[\s+(-?\d+(?:\.\d+)?(?:,\s*-?\d+(?:\.\d+)?)+)\s+]',
                     lambda m: "[" + re.sub(r'\s+', '', m.group(1)) + "]", raw)
    with open(path, "w", encoding="utf-8") as f:
        f.write(compact)


# === File Generation ===
os.makedirs(f"{base_model_path}/bamboo_flute", exist_ok=True)

for i, color in enumerate(colors):
    # base variant
    base_model = {
        "parent": "item/generated",
        "textures": {
            "layer0": f"{texture_path}{color}"
        },
        "display": base_display,
        "overrides": [
            {
                "predicate": {
                    "playing": 1
                },
                "model": f"{namespace}:item/playing_bamboo_flute_{color}"
            }
        ]
    }

    write_json(f"{base_model_path}/bamboo_flute/bamboo_flute_{color}.json", base_model)

    # playing variant
    playing_model = {
        "parent": "item/generated",
        "textures": {
            "layer0": f"{playing_texture_path}{color}"
        },
        "display": playing_display
    }

    write_json(f"{base_model_path}/playing_bamboo_flute_{color}.json", playing_model)

# master override model
main_model = {
    "parent": "item/generated",
    "textures": {
        "layer0": f"{namespace}:item/bamboo_flute"
    },
    "display": base_display,
    "overrides": [
        {
            "predicate": {
                "color": round(i / 16, 4)
            },
            "model": f"{namespace}:item/bamboo_flute/bamboo_flute_{color}"
        } for i, color in enumerate(colors)
    ] + [
        {
            "predicate": {
                "color": -1
            },
            "model": f"{namespace}:item/bamboo_flute"
        }
    ]
}

write_json(f"{base_model_path}/bamboo_flute.json", main_model)
print("✔ All JSON files written with compact formatting.")
