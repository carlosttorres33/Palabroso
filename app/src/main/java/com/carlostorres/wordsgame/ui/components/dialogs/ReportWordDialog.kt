package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.carlostorres.wordsgame.ui.components.CornerButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.ui.theme.ROUND_CORNER_SIZE
import com.carlostorres.wordsgame.utils.ButtonPlace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportWordDialog(
    word: String,
    onReportClick: () -> Unit,
    onCancelClick: () -> Unit
) {

    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    val listOfReasons = listOf(
        "La palabra no existe",
        "La palabra está mal escrita",
        "Palabra en plural",
        "No sé qué significa",
        "Palabra extraña",
        "Otra Razón"
    )

    var selectedReason by remember {
        mutableStateOf(listOfReasons[0])
    }

    Dialog(onDismissRequest = {}, properties = DialogProperties(dismissOnBackPress = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
            ),
            shape = RoundedCornerShape(ROUND_CORNER_SIZE.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .padding(horizontal = 32.dp),
                    text = "¿Qué está mal con la palabra?",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = word.uppercase(),
                    color = if (isSystemInDarkTheme()) DarkRed else LightRed,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = dropdownMenuExpanded,
                    onExpandedChange = {
                        dropdownMenuExpanded = !dropdownMenuExpanded
                    }
                ) {

                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .menuAnchor()
                            .clickable {
                                dropdownMenuExpanded = !dropdownMenuExpanded
                            },
                        value = selectedReason,
                        readOnly = true,
                        onValueChange = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = dropdownMenuExpanded
                            )
                        }
                    )

                    DropdownMenu(
                        modifier = Modifier.menuAnchor(),
                        expanded = dropdownMenuExpanded,
                        onDismissRequest = {
                            dropdownMenuExpanded = false
                        }
                    ) {

                        listOfReasons.forEachIndexed { index, reason ->

                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = reason
                                    )
                                },
                                onClick = {
                                    selectedReason = listOfReasons[index]
                                    dropdownMenuExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )

                        }

                    }
                }



                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    CornerButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        buttonPlace = ButtonPlace.BottomStart,
                        buttonText = "Reportar",
                        textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                    ) {
                        onReportClick()
                    }

                    VerticalDivider(modifier = Modifier.fillMaxHeight())

                    CornerButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        buttonPlace = ButtonPlace.BottomEnd,
                        buttonText = "Cancelar",
                        textColor = DarkYellow
                    ) {
                        onCancelClick()
                    }

                }

            }
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReportWordDialogPreview() {
    ReportWordDialog(word = "Pato", onReportClick = { /*TODO*/ }) {

    }
}